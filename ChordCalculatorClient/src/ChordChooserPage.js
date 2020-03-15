import React from 'react';
import './App.css';
import axios from 'axios';
import Instruments from "./Instruments";
import myURLs from './myURLs.js';
import FavoritListView from './FavoritListView';
import CatchView from './CatchView.js';
import MIDISounds from 'midi-sounds-react';

class ChordView extends React.Component {
    componentDidMount(){
        this.midiSounds.cacheInstrument(256);
    }
    clickOnCatchView = (catcha)=>{
        this.midiSounds.cancelQueue();
        let selectedInstrument = 256;
        let counter = 0;
        catcha.stringCatches.slice().reverse().forEach(stringCatch=>{
            if (stringCatch.midiCode!=0){
                this.midiSounds.playChordAt(this.midiSounds.contextTime() + 0.5 * counter++, selectedInstrument, [stringCatch.midiCode], 1);
            
        }});
        }
    render() {
        const {catchLoaded, catches, bundDif, favoritLists, token, instrument, chord} = this.props;
        return (
            <React.Fragment>
                {(catchLoaded) ? (           
                    catches.slice(0,10).map(catcha => {
                            return (
                                    <CatchView view="page" chord={chord} catcha={catcha} bundDif={bundDif} click={this.clickOnCatchView} favoritLists={favoritLists} token={token} instrument={instrument}></CatchView>
                            );
                    })
                ) : (
                    <h3>Loading...</h3>
                )}
                <div hidden={true}>
                    <MIDISounds ref={(ref) => (this.midiSounds = ref)} appElementName="root" instruments={[3]} />
                </div>
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
                console.log(this.chordComponentURL);
                console.log(res);
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
    sendFreeTextChord = (e) =>{
        if (e.key==='Enter'){
            let freeText = document.getElementById("freeTextChord").value;
            if (!freeText) return;
            freeText = freeText.replace(/[/]/g,'>');
            freeText = freeText.replace(/[.]/g,'<');
            axios.get(this.freeTextChordURL+encodeURIComponent(freeText))
                .then(res => {
                    console.log(this.freeTextChordURL);
                    console.log(res);
                    document.getElementById('baseSoundSelect').value=res.data.baseSound;
                    document.getElementById('baseTypeSelect').value=res.data.baseType;
                    document.getElementById('chordTypeSelect').value=res.data.chordType;
                    this.ancestor.setState({baseSound: res.data.baseSound,
                                            baseType: res.data.baseType,
                                            chordType: res.data.chordType});
                    this.ancestor.catchQuery();
                }).catch(error => {
                    //this.setState({error, isLoaded: false});
                    alert("Bad Expression");
                });
        }
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
                                       onKeyDown={this.sendFreeTextChord} autoFocus={true} ></input>
                                <select className="form-control p-2 flex-fill bd-highlight bgPrim" onChange={this.baseSoundChange} value={this.state.value} id="baseSoundSelect">
                                    {baseSounds.map((baseSound) =>
                                        <option  value={baseSound.name}>{baseSound.label}</option>
                                    )}
                                </select>
                                <select className="form-control p-2 flex-fill bd-highlight bgPrim" onChange={this.baseTypeChange} value={this.state.value} id="baseTypeSelect">
                                    {baseTypes.map((baseType) =>
                                        <option  value={baseType.name}>{baseType.label}</option>
                                    )}
                                </select>
                                <select className="form-control p-2 flex-fill bd-highlight bgPrim" onChange={this.chordTypeChange} value={this.state.value} id="chordTypeSelect">
                                    {chordTypes.map((chordType) =>
                                        <option  value={chordType.name}>{chordType.label}</option>
                                    )}
                                </select>
                                <div hidden={this.ancestor.props.token==undefined}>
                                <FavoritListView favoritLists={this.ancestor.props.favoritLists} userToken={this.props.userToken}></FavoritListView>
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
        capo: 0,

        instrumental: {},
        bundDif : 5,

        //strings: [],
        catches: [],
        catchLoaded: false,
        stringLoaded: false,
    }

    setBaseSound(baseSound){
        this.state.baseSound =  baseSound;
        //this.setState({baseSound :baseSound});
        this.catchQuery();
    }
    setBaseType(baseType){
        this.state.baseType = baseType;
        //this.setState({baseType :baseType});
        this.catchQuery();
    }
    setChordType(chordType){
        this.state.chordType = chordType;
        //this.setState({chordType :chordType});
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
    render() {
        const {catches , catchLoaded, bundDif, instrumental, chord} = this.state;
        return (
            <div>
                <ChordChooserList ancestor={this} userToken={this.props.token}></ChordChooserList>
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