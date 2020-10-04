import React from 'react';
import './App.css';
import axios from "axios";
import Dashboard from './Modal/Dashboard';
import myURLs from './myURLs.js';
import EditableModalDialog from './Modal/EditableModalDialog';

class Instruments extends React.Component {
    state={instruments : [],
            loaded : false,
            error: null,
            newInstrumentalStrings: [],
            editMode: false,
            detailedInstrument: undefined
    };
    componentDidMount() {
        let instrumentURL = myURLs.getURL() + "instrument/" + this.props.chordChooserList.ancestor.props.token;
        axios.get(encodeURI(instrumentURL))
            .then(res => {
                console.log(res.data);
                this.setState(
                    {
                        instruments: res.data,
                        loaded: true,
                    });
                this.setInstrument(res.data[0]);
            }).catch(error => this.setState({error, loaded: false}));
    }
    addNewInstrumentString(){
        let sel = document.getElementById("baseSoundSelect");
        let refreshedStrings = this.state.newInstrumentalStrings;
        refreshedStrings.push({'label' : sel.options[sel.selectedIndex].text,
                                'name' : sel.options[sel.selectedIndex].value,
        });
        this.setState({newInstrumentalStrings : refreshedStrings})
    }
    setInstrument(instrumental){
        this.props.chordChooserList.instrumentalChange(instrumental);
    }
   
    newInstrument(){

        let newInstrumentURL = myURLs.getURL() + "instrument/new/";
        let passedParameters = {
            "userToken": this.ancestor.props.chordChooserList.ancestor.props.token,
            "name": document.getElementById("instrumentName").value,
            "maxBundDif": document.getElementById("maxBundDif").value,
            "strings": this.ancestor.state.newInstrumentalStrings,
            "bundNumber": document.getElementById("bundNumber").value
        };
        this.ancestor.setState({newInstrumentalStrings : [] });
        axios.post(newInstrumentURL, passedParameters)
            .then(res=>{
                if (res){
                    console.log("New instrumental added.");
                    var newInstruments = this.ancestor.state.instruments;
                    newInstruments.push({"name":passedParameters.instrumentalName});
                    this.ancestor.setState({instruments:newInstruments});
                    window.location.reload();
                }
                else {
                    console.log("Instrumental adding failed.");
                }
                
        })
    }
    cancelCreateInstrument(){
        this.ancestor.setState({newInstrumentalStrings : [] });
        console.log("Modal cancelled.");
    }
    cancelEditInstrument = () => {
        this.setState({newInstrumentalStrings : [], detailedInstrument: undefined, editMode: false});
        console.log("Modal cancelled.");
    };
    approveEditInstrument = () => {
        let editInstrumentURL = myURLs.getURL()+"instrument/edit/"+this.state.detailedInstrument.instrumentToken;
        let passedParameters = {
            "name": document.getElementById("instrumentName").value,
            "maxBundDif": document.getElementById("maxBundDif").value,
            "bundNumber": document.getElementById("bundNumber").value,
            "strings": this.state.newInstrumentalStrings
        };
        console.log(passedParameters);
        axios.post(editInstrumentURL, passedParameters)
            .then(res=>{
                console.log(res);
                window.location.reload();
        });
        
        this.setState({newInstrumentalStrings : [], detailedInstrument: undefined, editMode: false});
    };
    showDetails(instrumental){
        console.log("show details of "+instrumental.name);
        this.setState({detailedInstrument: Object.assign({},instrumental),
                        newInstrumentalStrings: Object.assign([],instrumental.strings).reverse()})
    }

    removeInstrument=()=>{
        console.log(this);
        let deleteInstrumentURL = myURLs.getURL()+"instrument/delete/"+this.state.detailedInstrument.instrumentToken;
        axios.delete(deleteInstrumentURL).then(res=>{
            window.location.reload();
        });
        this.setState({detailedInstrument:undefined});
    }
    handleEdit=()=>{
        console.log(this.state.detailedInstrument);
        this.setState({editMode : true,
        newInstrumentalStrings: this.state.detailedInstrument.strings});
    }
    deleteString(string){
        var strings = this.state.newInstrumentalStrings;
        var index = strings.indexOf(string)
        if(index>-1){
            strings.splice(index,1);
        }
        this.setState({newInstrumentalStrings: strings});
    }
    render(){
        const {loaded, instruments, newInstrumentalStrings,detailedInstrument} = this.state;
        let isPublic = detailedInstrument!=undefined ? detailedInstrument.isPublic : false;
        return (<React.Fragment>
                    <nav className="navbar navbar-expand-sm navbar-light">
                        <button className="navbar-toggler" type="button" data-toggle="collapse"
                                data-target="#navbarSupportedContent" aria-controls="navbarSupportedContent"
                                aria-expanded="false" aria-label="Toggle navigation">
                            <span className="navbar-toggler-icon"></span>
                        </button>
                        <div className="collapse navbar-collapse" id="navbarSupportedContent">
                            {((loaded) ? (
                                    <div>
                                    {instruments.map(
                                        instrumental =>{
                                        return (
                                                <span>
                                                    <button type="button" class="btn btn-outline-dark nav-item btn-sm minibutton" onClick={()=>this.showDetails(instrumental)}><i class="fa fa-edit"></i></button>
                                                    <button className="btn btn-outline-success nav-item instrument" onClick={() =>this.setInstrument(instrumental)} type="button">{instrumental.name}</button>                                                    
                                                </span>
                                        );
                                        })}

                                    </div>

                                ) : (<h2>Loading...</h2>))}
                        </div>
                        <div hidden={this.props.chordChooserList.ancestor.props.token==undefined}>
                        <Dashboard title="New" onAccept={this.newInstrument} onReject={this.cancelCreateInstrument} ancestor={this}>
                            <div className="container">
                            <div className="row">
                            <div className="col-lg-6">
                                <label htmlFor="instumentName">Instrument name</label>
                                <input type="text" className="form-control" id="instrumentName" placeholder="Instrument name" autofocus="true"/>
                            </div>
                            <div className="col-lg-3">
                            <label htmlFor="maxBundDif">Maximum bund difference</label>
                            <input type="number" className="form-control" id="maxBundDif" placeholder="Maximum bund different" defaultValue="4"/>
                            </div>
                            <div className="col-lg-3">
                            <label htmlFor="bundNumber">Bund number</label>
                            <input type="number" className="form-control" id="bundNumber" placeholder="Bund number" defaultValue="14"/>
                            </div>
                            </div>
                            <div>
                                <h2>Strings</h2>
                                <select id="baseSoundSelect">
                                    {this.props.chordChooserList.state.baseSounds.map(bs =>{
                                        return (<option  value={bs.name}>{bs.label}</option>)
                                    })}
                                </select>
                                <button onClick={() =>this.addNewInstrumentString()}>Add</button>
                            </div>
                                <div className="row">
                            {newInstrumentalStrings.map(
                                string =>{
                                    return(
                                        <div className="col-lg-2">
                                            <p>{string.label}</p>
                                            <button onClick={()=>this.deleteString(string)}>Delete</button>
                                        </div>)
                            })}
                            </div>
                            </div>
                        </Dashboard>
                        </div>
                        <EditableModalDialog title={this.state.editMode? "Edit instrument":"Instrument details"}  
                        show={this.state.detailedInstrument!=undefined}
                        isPublic={isPublic}
                        approveEdit={this.approveEditInstrument}
                        handleEdit={this.handleEdit}
                        handleDelete={this.removeInstrument}
                        handleReject={this.cancelEditInstrument}
                        editMode={this.state.editMode} >
                            <div className="container">
                            <div className="row">
                            <div className="col-lg-6">
                                <label htmlFor="instumentName">Instrument name</label>
                                <input type="text" className="form-control" id="instrumentName" placeholder="Instrument name" autofocus="true" defaultValue={this.state.detailedInstrument ? this.state.detailedInstrument.name : ""} readOnly={!this.state.editMode}/>
                            </div>
                            <div className="col-lg-3">
                            <label htmlFor="maxBundDif">Maximum bund</label>
                            <input readOnly={!this.state.editMode} type="number" className="form-control" id="maxBundDif" placeholder="Maximum bund" defaultValue={this.state.detailedInstrument ? this.state.detailedInstrument.maxBundDif: ""}/>
                            </div>
                            <div className="col-lg-3">
                            <label htmlFor="bundNumber">Bund number</label>
                            <input readOnly={!this.state.editMode} type="number" className="form-control" id="bundNumber" placeholder="Bund number" defaultValue={this.state.detailedInstrument ? this.state.detailedInstrument.bundNumber: ""}/>
                            </div>
                            </div>
                            <div>
                                <h2>Strings</h2>
                                <select hidden={!this.state.editMode} id="baseSoundSelect">
                                    {this.props.chordChooserList.state.baseSounds.map(bs =>{
                                        return (<option  value={bs.name}>{bs.label}</option>)
                                    })}
                                </select>
                                <button hidden={!this.state.editMode} onClick={() =>this.addNewInstrumentString()}>Add</button>
                            </div>
                                <div className="row">
                            {newInstrumentalStrings.map(
                                string =>{
                                    return(
                                        <div className="col-lg-2 col-md-2">
                                            <p>{string.label}</p>
                                            <button hidden={!this.state.editMode} onClick={()=>this.deleteString(string)}>Delete</button>
                                        </div>)
                            })}
                            </div>
                            </div>
                        </EditableModalDialog>
                    </nav>

                </React.Fragment>
        );
    }
}

export default Instruments;