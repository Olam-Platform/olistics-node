pragma solidity ^0.4.24;


contract Transport {
    struct Document {
        string url;
        address submitter;
        uint timeStamp;
        address[] recipients;
        mapping(address => bytes32) keys;
    }

    // constants
    string constant MANAGER_ROLE = "manager";
    string constant SHIPPER_ROLE = "shipper";
    string constant RECEIVER_ROLE = "receiver";

    uint startTime;                             // start time of this contract
    string currentState;                        // current transport state

    mapping(string => Document[])   documents;                      // all submitted documents for this transport
    mapping(string => bool)         documentsExist;                 // helper mapping
    mapping(string => address)      roles;                          // all transport roles
    address[]                       addresses;


    mapping(address => string[])    directory;                      //

    // events
    event TransportStarted(uint timeStamp, address indexed recipient);
    event DocumentSubmitted(string name, uint version, address recipient);
    event DocumentRequested(string name, uint version, address submitter);
    event StateChanged(string previousState, string currentState, string cause, address recipient);

    event Warn(string message);

    constructor(address shipperAddress, address receiverAddress, uint timeStamp) public {
        roles[MANAGER_ROLE] = msg.sender;       // the creator of this Transport
        roles[SHIPPER_ROLE] = shipperAddress;   // the origin of the goods
        roles[RECEIVER_ROLE] = receiverAddress; // the final destination of the goods

        addresses.push(roles[MANAGER_ROLE]);
        addresses.push(roles[SHIPPER_ROLE]);
        addresses.push(roles[RECEIVER_ROLE]);

        directory[msg.sender].push(MANAGER_ROLE);
        directory[shipperAddress].push(SHIPPER_ROLE);
        directory[receiverAddress].push(RECEIVER_ROLE);

        startTime = timeStamp;
        currentState = "none";

        // notify all roles that the transport has started
        for (uint i = 0; i < addresses.length; i++) {
            emit TransportStarted(timeStamp, addresses[i]);
        }
    }

    // submit a document
    function submitDocument(
        string name, string url, uint timeStamp, address[] recipients, bytes32[] keys) public returns (uint
    ) {
        require(isKnownRole(msg.sender), "role is not part of this transport");

        //mapping(address => string) storage keysMapping;
        documents[name].push(
            Document({url : url, submitter : msg.sender, timeStamp : timeStamp, recipients : new address[](0)})
        );

        uint version = documents[name].length - 1;
        documentsExist[name] = true;

        for (uint i; i < recipients.length; i++) {
            if (isKnownRole(recipients[i])) {
                documents[name][version].recipients.push(recipients[i]);
                documents[name][version].keys[recipients[i]] = keys[i];

                emit DocumentSubmitted(name, version, recipients[i]);
            } else {
                emit Warn("document recipient unknown");
            }
        }

        return version;
    }

    // request a document locator
    function requestDocument(string name) public view returns (string, uint, address, uint/*, bytes32*/) {
        require(isKnownRole(msg.sender), "role is not part of this transport");
        require(documentsExist[name], "unknown document requested");

        // return the latest version
        uint version = documents[name].length - 1;
        return requestDocument(name, version);
    }

    // request a versioned document locator
    function requestDocument(
        string name, uint version) public view returns (string, uint, address, uint/*, bytes32*/
    ) {
        require(isKnownRole(msg.sender), "role is not part of this transport");
        require(documentsExist[name], "unknown document requested");
        require(version < documents[name].length, "illegal version number requested");

        address submitter = documents[name][version].submitter;
        uint timeStamp = documents[name][version].timeStamp;

        //emit DocumentRequested(name, version, submitter);

        return (
        documents[name][version].url, version, submitter, timeStamp/*, documents[name][version].keys[msg.sender]*/
        );
    }

    // does not validate state transitions
    function setState(string newState, string cause) public {
        require(isKnownRole(msg.sender), "role is not part of this transport");

        for (uint i = 0; i < addresses.length; i++) {
            emit StateChanged(currentState, newState, cause, addresses[i]);
        }

        currentState = newState;
    }

    function getState() public view returns (string) {
        require(isKnownRole(msg.sender), "role is not part of this transport");

        return currentState;
    }

    function addRole(string role, address roleAddress) public {
        require(msg.sender == roles[MANAGER_ROLE], "only manager is allowed to add roles");

        roles[role] = roleAddress;
        addresses.push(roleAddress);
        directory[roleAddress].push(role);
    }

    function getRole(address roleAddress) public view returns (string) {
        require(isKnownRole(msg.sender), "role is not part of this transport");

        return directory[roleAddress][0];
    }

    function getAddress(string role) public view returns (address) {
        return roles[role];
    }

    function isKnownRole(address roleAddress) private view returns (bool) {
        bool result = false;

        for (uint i = 0; i < directory[roleAddress].length; i++) {
            if (bytes(directory[roleAddress][i]).length > 0) {
                result = true;
                break;
            }
        }

        return result;
    }
}
