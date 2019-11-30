import React from 'react';
import './App.css';
import axios from 'axios';
import Instruments from "./Instruments";
import myURLs from './myURLs.js';
import FavoritListView from './FavoritListView';
import CatchView from './CatchView.js';
class ChordView extends React.Component {
    render() {
        const {catchLoaded, catches, bundDif, favoritLists, token, instrument, chord} = this.props;
        return (
            <React.Fragment>
                {(catchLoaded) ? (
                    catches.slice(0,10).map(catcha => {
                            return (
                                <CatchView view="page" chord={chord} catcha={catcha} bundDif={bundDif} favoritLists={favoritLists} token={token} instrument={instrument}></CatchView>
                            );
                    })  
                ) : (
                    <h3>Loading...</h3>
                )}
            </React.Fragment>
        )
    }
}
class ChordChooserList extends React.Component{

    state={
        baseSounds: [],
        baseTypes: [],
        chordTypes: [],
        perfectExpression: "",
        isLoaded : false,
    }

    ancestor;

    chordComponentURL;
    freeTextChordURL;

    constructor(props) {
        super(props);
        this.ancestor = props.ancestor;
        this.chordComponentURL = myURLs.getURL()+"chord/components/";
        this.freeTextChordURL = myURLs.getURL()+"chord/text/";
        this.baseSoundChange = this.baseSoundChange.bind(this);
        this.baseTypeChange = this.baseTypeChange.bind(this);
        this.chordTypeChange = this.chordTypeChange.bind(this);
    }

    componentDidMount() {
        axios.get(this.chordComponentURL)
            .then(res => {
                this.setState(
                    {
                        baseSounds: res.data["baseSounds"],
                        baseTypes: res.data["baseTypes"],
                        chordTypes: res.data["chordTypes"],
                        isLoaded: true
                    });
            }).catch(error => this.setState({error, isLoaded: false}));
    }
    baseSoundChange(e){
        this.ancestor.setBaseSound(e.target.value)
    }
    baseTypeChange(e){
        this.ancestor.setBaseType(e.target.value);
    }
    chordTypeChange(e){
        this.ancestor.setChordType(e.target.value);
    }
    instrumentalChange(instr){
        this.ancestor.setInstrument(instr);
    }
    sendFreeTextChord(){
        let freeText = document.getElementById("freeTextChord").value;
        if (!freeText) return;
        axios.get(this.freeTextChordURL+encodeURIComponent(freeText))
            .then(res => {
                if (res.data.error!=null){
                    alert(res.data.error);
                }
                else {
                    document.getElementById('baseSoundSelect').value=res.data.bs;
                    document.getElementById('baseTypeSelect').value=res.data.bt;
                    document.getElementById('chordTypeSelect').value=res.data.ct;
                    this.ancestor.setState({baseSound: res.data.bs,
                                            baseType: res.data.bt,
                                            chordType: res.data.ct});
                    this.ancestor.catchQuery();
                }

            }).catch(error => this.setState({error, isLoaded: false}));
    }
    render(){
        const {baseSounds, baseTypes, chordTypes, isLoaded, perfectExpression} = this.state;
        return(
            <div>
                {(isLoaded) ? (
                    <React.Fragment>
                        <Instruments chordChooserList={this}></Instruments>
                        <div>
                            <div className="d-flex bd-highlight">
                                <input className="form-control centered bgPrim" id="freeTextChord" type="text" placeholder="Type chord!"
                                       onKeyUp={() => this.sendFreeTextChord()} autoFocus={true} ></input>
                                <select className="form-control p-2 flex-fill bd-highlight bgPrim" onChange={this.baseSoundChange} value={this.state.value} id="baseSoundSelect">
                                    {baseSounds.map((bs) =>
                                        <option  value={bs.name}>{bs.label}</option>
                                    )}
                                </select>
                                <select className="form-control p-2 flex-fill bd-highlight bgPrim" onChange={this.baseTypeChange} value={this.state.value} id="baseTypeSelect">
                                    {baseTypes.map((bt) =>
                                        <option  value={bt.name}>{bt.label}</option>
                                    )}
                                </select>
                                <select className="form-control p-2 flex-fill bd-highlight bgPrim" onChange={this.chordTypeChange} value={this.state.value} id="chordTypeSelect">
                                    {chordTypes.map((ct) =>
                                        <option  value={ct.name}>{ct.label}</option>
                                    )}
                                </select>
                                <div hidden={this.ancestor.props.token==undefined}>
                                <FavoritListView favoritLists={this.ancestor.props.favoritLists}></FavoritListView>
                                </div>
                                
                            </div>
                        </div>
                    </React.Fragment>
                ) : (<h3></h3>)}
            </div>
        )
    }
}

class ChordChooserPage extends React.Component {
    state={
        baseSound : "C",
        baseType: "maj",
        chordType: "n",
        instrumental: {},
        bundDif : 5,

        //strings: [],
        catches: [],
        catchLoaded: false,
        stringLoaded: false,
    }

    setBaseSound(bs){
        this.state.baseSound =  bs;
        //this.setState({baseSound :bs});
        this.catchQuery();
    }
    setBaseType(bt){
        this.state.baseType = bt;
        //this.setState({baseType :bt});
        this.catchQuery();
    }
    setChordType(ct){
        this.state.chordType = ct;
        //this.setState({chordType :ct});
        this.catchQuery();
    }
    setInstrument(inst){
        this.state.instrumental = inst;
        //this.stringQuery();
        this.catchQuery();
    }

    isValid() {
        return (this.state.instrumental && this.state.instrumental.token != null && this.state.baseSound != null && this.state.baseType != null && this.state.chordType != null);
    }

    catchQuery(){
            if (this.isValid()) {
            let catchURL = myURLs.getURL() + "chord/catch/" + this.state.instrumental.token + "/" + this.state.baseSound + "/" + this.state.baseType + "/" + this.state.chordType;
                axios.get(catchURL)
                    .then(res => {
                        console.log(res.data);
                        this.setState(
                            {
                                catches: res.data.catches,
                                chord: res.data.chord,
                                bundDif: res.data.bundDif,
                                catchLoaded: true
                            });

                    }).catch(error => this.setState({error, catchLoaded: false}));
            }
    }
    /*stringQuery(){
        if (this.state.instrumental) {
            let stringURL = myURLs.getURL() + "instrument/strings/" + this.state.instrumental.token;
            axios.get(stringURL)
                .then(res => {
                    this.setState(
                        {
                            strings: res.data,
                            stringLoaded: true
                        });
                }).catch(error => this.setState({error, stringLoaded: false}));
        }
    }*/
    render() {
        const {catches , catchLoaded, bundDif, instrumental, chord} = this.state;
        return (
            <div>
                <ChordChooserList ancestor={this}></ChordChooserList>
                <div className="container">
                    <div className="row">
                        {( this.isValid()) ?
                            <ChordView catches={catches} chord={chord} catchLoaded={catchLoaded} bundDif = {bundDif} favoritLists = {this.props.favoritLists} token={this.props.token} instrument={instrumental}></ChordView>
                            : (<h3> </h3>)}
                    </div>
                </div>
            </div>
        );
    }
}
export default ChordChooserPage;