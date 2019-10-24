import React from 'react';
import './App.css';
import axios from "axios";
import Dashboard from './Modal/Dashboard';
import myURLs from './myURLs.js';
import ModalDialog from './Modal/ModalDialog';

class Instruments extends React.Component {
    state={instruments : [],
            loaded : false,
            error: null,
            newInstrumentalStrings: [],
    };
    componentDidMount() {
        let instrumentURL = myURLs.getURL() + "instrumental/" + this.props.chordChooserList.ancestor.props.token;
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
        refreshedStrings.push({'name' : sel.options[sel.selectedIndex].text,
                                'value' : sel.options[sel.selectedIndex].value,
        });
        this.setState({newInstrumentalStrings : refreshedStrings})
    }
    setInstrument(instrumental){
        this.props.chordChooserList.instrumentalChange(instrumental);
    }
    editInstrument(instrumental){
        console.log("edit "+instrumental.name);
        this.setState({editableInstrument: Object.assign({},instrumental),
                            newInstrumentalStrings: Object.assign([],instrumental.strings).reverse()})
    }
    removeInstrument(instrumental){
        let deleteInstrumentURL = myURLs.getURL()+"deleteinstrument/"+instrumental.token;
        axios.delete(deleteInstrumentURL).then(res=>{
            window.location.reload();
            alert(res.data);
        });  
    }
    newInstrument(){

        let newInstrumentURL = myURLs.getURL() + "newinstrument/";
        let passedParameters = {
            "user": this.ancestor.props.chordChooserList.ancestor.props.token,
            "instrumentalName": document.getElementById("instrumentName").value,
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
    canceledModal(){
        this.ancestor.setState({newInstrumentalStrings : [] });
        console.log("Modal cancelled.");
    }
    handleReject = () => {
        this.setState({newInstrumentalStrings : [], editableInstrument: undefined});
        console.log("Modal cancelled.");
        console.log(this);
    };
    handleAccept = () => {
        let editInstrumentURL = myURLs.getURL()+"editinstrument/"+this.state.editableInstrument.token;
        let passedParameters = {
            "instrumentalName": document.getElementById("instrumentName").value,
            "maxBundDif": document.getElementById("maxBundDif").value,
            "bundNumber": document.getElementById("bundNumber").value,
            "strings": this.state.newInstrumentalStrings
        };
        axios.post(editInstrumentURL, passedParameters)
            .then(res=>{
                console.log(res);
                window.location.reload();
        });


        this.setState({newInstrumentalStrings : [], editableInstrument: undefined});
    };
    deleteString(string){
        var strings = this.state.newInstrumentalStrings;
        var index = strings.indexOf(string)
        if(index>-1){
            strings.splice(index,1);
        }
        this.setState({newInstrumentalStrings: strings});
    }
    render(){
        const {loaded, instruments, newInstrumentalStrings} = this.state;
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
                                                    <button type="button" class="btn btn-warning btn-sm minibutton" onClick={()=>this.editInstrument(instrumental)} hidden={instrumental.public}><i class="fa fa-edit"></i></button>
                                                    <button className="btn btn-outline-success nav-item" onClick={() =>this.setInstrument(instrumental)} type="button">{instrumental.name}</button>                                                    
                                                    <button type="button" class="btn btn-danger btn-sm minibutton" onClick={()=>this.removeInstrument(instrumental)} hidden={instrumental.public}><i class="fa fa-close"></i></button>
                                                </span>
                                        );
                                        })}

                                    </div>

                                ) : (<h2>Loading...</h2>))}
                        </div>
                        <div hidden={this.props.chordChooserList.ancestor.props.token==undefined}>
                        <Dashboard title="New" onAccept={this.newInstrument} onReject={this.canceledModal} ancestor={this}>
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
                                            <p>{string.name}</p>
                                            <button onClick={()=>this.deleteString(string)}>Delete</button>
                                        </div>)
                            })}
                            </div>
                            </div>
                        </Dashboard>
                        </div>
                        <ModalDialog title="Edit" show={this.state.editableInstrument!=undefined} handleAccept={this.handleAccept} handleReject={this.handleReject} >
                            <div className="container">
                            <div className="row">
                            <div className="col-lg-6">
                                <label htmlFor="instumentName">Instrument name</label>
                                <input type="text" className="form-control" id="instrumentName" placeholder="Instrument name" autofocus="true" defaultValue={this.state.editableInstrument ? this.state.editableInstrument.name: " "}/>
                            </div>
                            <div className="col-lg-3">
                            <label htmlFor="maxBundDif">Maximum bund difference</label>
                            <input type="number" className="form-control" id="maxBundDif" placeholder="Maximum bund different" defaultValue={this.state.editableInstrument ? this.state.editableInstrument.maxBundDif: ""}/>
                            </div>
                            <div className="col-lg-3">
                            <label htmlFor="bundNumber">Bund number</label>
                            <input type="number" className="form-control" id="bundNumber" placeholder="Bund number" defaultValue={this.state.editableInstrument ? this.state.editableInstrument.bundNumber: ""}/>
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
                                        <div className="col-lg-2 col-md-2">
                                            <p>{string.name}</p>
                                            <button onClick={()=>this.deleteString(string)}>Delete</button>
                                        </div>)
                            })}
                            </div>
                            </div>
                        </ModalDialog>
                    </nav>

                </React.Fragment>
        );
    }
}

export default Instruments;