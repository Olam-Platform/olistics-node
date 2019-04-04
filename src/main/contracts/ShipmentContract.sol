pragma solidity ^0.5.2;
//pragma experimental ABIEncoderV2;

contract ShipmentContract {
    enum DocumentEventEnum {
        DocumentAdded,
        DocumentUpdated,
        DocumentRequested
    }

    struct Document {
        string  name;
        string  url;
        address owner;
        address collaboratorAddress;
        bool    isValid;
    }

    struct Collaborator {
        string  name;
        string  role;
        address publicAddress;
        bool    isValid;
    }

    // collaborators roles
    string constant OWNER_ROLE      = "owner";
    string constant SHIPPER_ROLE    = "shipper";
    string constant CONSIGNEE_ROLE  = "consignee";


    uint    timeStart;                                  // start time of this contract
    string  state;                                      // current transport state
    string  shipmentName;

    mapping(address => Collaborator)    collaborators;      // maps addresses to collaborators
    mapping(string  => Collaborator)    roles;              // maps roles to collaborators
    mapping(string  => Document)        documents;          // maps document names to documents

    // events
    event DocumentEvent(string name, string url, DocumentEventEnum docEvent, address trigerredBy, address collaboratorAddress);
    event StateChanged(string oldState, string state, address changedBy);
    event CollaboratorAdded(string name, string role, address publicAddress);
    event Warn(string message);

    constructor(
        string memory _shipmentName, string memory ownerName,
        address shipperAddress, string memory shipperName,
        address consigneeAddress, string memory consigneeName
    ) public {
        shipmentName = _shipmentName;

        Collaborator memory owner;
        owner.publicAddress = msg.sender;
        owner.name = ownerName;
        owner.role = OWNER_ROLE;
        owner.isValid = true;
        collaborators[owner.publicAddress] = owner;
        roles[OWNER_ROLE] = owner;

        Collaborator memory shipper;
        shipper.publicAddress = shipperAddress;
        shipper.name = shipperName;
        shipper.role = SHIPPER_ROLE;
        shipper.isValid = true;
        collaborators[shipper.publicAddress] = shipper;
        roles[SHIPPER_ROLE] = shipper;

        Collaborator memory consignee;
        consignee.publicAddress = consigneeAddress;
        consignee.name = consigneeName;
        consignee.role = CONSIGNEE_ROLE;
        consignee.isValid = true;
        collaborators[consignee.publicAddress] = consignee;
        roles[CONSIGNEE_ROLE] = consignee;

        state = "started";

        emit StateChanged(state, "none", owner.publicAddress);
    }

    // add a document
    function addDocument(string memory name, string memory url, address collaboratorAddress) public onlyByCollaborator {
        require(!documentExists(name), "document already exists, use updateDocument to update a document");

        Document memory document;
        document.name = name;
        document.url = url;
        document.owner = msg.sender;
        document.collaboratorAddress = collaboratorAddress;
        document.isValid = true;

        documents[name] = document;

        emit DocumentEvent(name, url, DocumentEventEnum.DocumentAdded, msg.sender, collaboratorAddress);
    }

    // update a document
    function updateDocument(string memory name, string memory url) public onlyByCollaborator {
        require(documentExists(name), "document does not exist, use addDocument to submit a new document");
        require(documents[name].owner == msg.sender, "only the document owner can update it");

        documents[name].url = url;

        emit DocumentEvent(name, url, DocumentEventEnum.DocumentUpdated, msg.sender, documents[name].collaboratorAddress);
    }

    // request a document locator
    function getDocument(string memory name) public view onlyByCollaborator returns(string memory url, address owner, address collaborator) {
        require(documentExists(name), "unknown document requested");
        require(documents[name].owner == msg.sender || documents[name].collaboratorAddress == msg.sender, "only owner or collaborator may access this document");

        //emit DocumentEvent(name, documents[name].url, DocumentEventEnum.DocumentRequested, msg.sender, documents[name].collaboratorAddress);

        return (documents[name].url, documents[name].owner, documents[name].collaboratorAddress);
    }

    // does not validate state transitions
    function setState(string memory newState) onlyByCollaborator public {
        string memory oldState = state;

        state = newState;

        emit StateChanged(oldState, state, msg.sender);
    }

    function getState() onlyByCollaborator public view returns (string memory) {
        return state;
    }

    function getName() onlyByCollaborator public view returns (string memory) {
        return shipmentName;
    }

    function addCollaborator(string memory name, string memory role, address collaboratorAddress) onlyByOwner public {
        require(roles[role].isValid == false, "the new collaborator must have a new role");
        Collaborator memory newCollaborator;

        newCollaborator.name = name;
        newCollaborator.role = role;
        newCollaborator.publicAddress = collaboratorAddress;
        newCollaborator.isValid = true;
        collaborators[newCollaborator.publicAddress] = newCollaborator;
        roles[role] = newCollaborator;

        emit CollaboratorAdded(name, role, collaboratorAddress);
    }

    function updateCollaborator(address collaboratorAddress, string memory name, string memory role) public {
        // only owner or the collaborator itself may update their profile data
        require(collaborators[collaboratorAddress].isValid == true);                        // collaborator exists
        require(msg.sender == collaboratorAddress || msg.sender == roles[OWNER_ROLE].publicAddress);    // updater is owner or the updatee

        collaborators[collaboratorAddress].name = name;
        collaborators[collaboratorAddress].role = role;
    }

    function getCollaboratorByAddress(address collaboratorAddress) public view returns(string memory, string memory, address) {
        Collaborator memory collaborator = collaborators[collaboratorAddress];

        return (collaborator.name, collaborator.role, collaborator.publicAddress);
    }

    function getCollaboratorByRole(string memory collaboratorRole) public view returns(string memory, string memory, address) {
        Collaborator memory collaborator = roles[collaboratorRole];

        return (collaborator.name, collaborator.role, collaborator.publicAddress);
    }

    modifier onlyByCollaborator() {
        require(collaborators[msg.sender].isValid, "unauthorised. not a collaborator to this shipment");
        _;
    }

    modifier onlyByOwner() {
        require(msg.sender == roles[OWNER_ROLE].publicAddress, "unauthorised. not the shipment owner");
        _;
    }

    function documentExists(string memory docName) private view returns (bool) {
        return (documents[docName].isValid);
    }
}
