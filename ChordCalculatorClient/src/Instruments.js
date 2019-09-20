import React from 'react';
import './App.css';
import axios from "axios";
import Dashboard from './Modal/Dashboard';
import myURLs from './myURLs.js';

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
                this.setState(
                    {
                        instruments: res.data,
                        loaded: true,
                    });
                this.setInstrument(res.data[0]);
            }).catch(error => this.setState({error, loaded: false}));
    }
    addNewInstrumentString(){
        var sel = document.getElementById("baseSoundSelect");
        var refreshedStrings = this.state.newInstrumentalStrings;
        refreshedStrings.push({'name' : sel.options[sel.selectedIndex].text,
                                'value' : sel.options[sel.selectedIndex].value,
        });
        this.setState({newInstrumentalStrings : refreshedStrings})
    }
    setInstrument(instrumental){
        this.props.chordChooserList.instrumentalChange(instrumental);
    }
    newInstrument(){

        let newInstrumentURL = myURLs.getURL() + "newinstrument/";
        let passedParameters = {
            "user": this.ancestor.props.chordChooserList.ancestor.props.token,
            "instrumentalName": document.getElementById("instrumentName").value,
            "maxBundDif": document.getElementById("maxBundDif").value,
            "firstSound": document.getElementById("firstSound").checked,
            "strings": this.ancestor.state.newInstrumentalStrings,
        };
        this.ancestor.setState({newInstrumentalStrings : [] });
        axios.post(newInstrumentURL, passedParameters)
            .then(res=>{
                if (res){
                    console.log("New instrumental added.");
                    var newInstruments = this.ancestor.state.instruments;
                    newInstruments.push({"name":passedParameters.instrumentalName})
                    this.ancestor.setState({instruments:newInstruments})
                }
                else {
                    console.log("Instrumental adding failed.");
                }
        })
    }
    canceledModal(){
        this.ancestor.setState({newInstrumentalStrings : [] });
        //console.log(this.ancestor.state);
        console.log("Modal cancelled.");
    }
    deleteString(string){
        var strings = this.state.newInstrumentalStrings;
        var index = strings.indexOf(string)
        if(index>-1){
            strings.splice(index,1);
        }
        //console.log(strings);
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
                                        return (<button className="btn btn-outline-success nav-item" onClick={() =>this.setInstrument(instrumental)} type="button">{instrumental.name}</button>);
                                        })}

                                    </div>

                                ) : (<h2>Loading...</h2>))}
                        </div>
                        <div hidden={this.props.chordChooserList.ancestor.props.token==undefined}>
                        <Dashboard title="New" onAccept={this.newInstrument} onReject={this.canceledModal} ancestor={this}>
                            <div className="container">
                            <div className="row">
                            <div className="col-lg-5">
                                <label htmlFor="instumentName">Instrument name</label>
                                <input type="text" className="form-control" id="instrumentName" placeholder="Instrument name" autofocus="true"/>
                            </div>
                            <div className="col-lg-5">
                            <label htmlFor="maxBundDif">Maximum bund difference</label>
                            <input type="number" className="form-control" id="maxBundDif" placeholder="Maximum bund different" defaultValue="4"/>
                            </div>
                            <div className="col-lg-2">
                                <label htmlFor="firstSound">First sound is root sound</label> <input type="checkBox" className="form-control" id="firstSound" defaultChecked/>
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
                    </nav>

                </React.Fragment>
        );
    }
}

export default Instruments;