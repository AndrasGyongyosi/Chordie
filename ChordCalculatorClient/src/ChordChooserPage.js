import React from 'react';
import './App.css';
import axios from 'axios';
import Instruments from "./Instruments";
import myURLs from './myURLs.js';
import ButtonDropDown from './ButtonDropDown';

class ChordView extends React.Component {
    render() {
        const {stringLoaded, catchLoaded, catches, strings, bundDif, favoritLists} = this.props;
        return (
            <React.Fragment>
                {(stringLoaded && catchLoaded) ? (
                    catches.slice(0,10).map(catcha => {
                            return (
                                <Catch catcha={catcha} strings={strings} bundDif={bundDif} favoritLists={favoritLists}></Catch>
                            );
                    })  
                ) : (
                    <h3>Loading...</h3>
                )}
            </React.Fragment>
        )
    }
}

class Catch extends React.Component{
    romanize (num) {
        if (isNaN(num))
            return NaN;
        var digits = String(+num).split(""),
            key = ["","C","CC","CCC","CD","D","DC","DCC","DCCC","CM",
                "","X","XX","XXX","XL","L","LX","LXX","LXXX","XC",
                "","I","II","III","IV","V","VI","VII","VIII","IX"],
            roman = "",
            i = 3;
        while (i--)
            roman = (key[+digits.pop() + (i * 10)] || "") + roman;
        return Array(+digits.join("") + 1).join("M") + roman;
    }

    getMinBund(catcha){
        var min = null;
        var arrayLength = catcha.length;
        for (var i=0;i<arrayLength; i++){
            let bund=catcha[i].bund;
            if(bund>0){
                if(min==null) min = bund;
                else if(min>bund) min = bund;
            }
        }
        if (min==2) min=1;
        return min;
    }
    getMaxBund(min){
        return min+this.props.bundDif;
    }
    range(min, max) {
        var len = max - min + 1;
        var arr = new Array(len);
        for (var i=0; i<len; i++) {
            arr[i] = min + i;
        }
        return arr;
    }

    getBunds(min, string, max){
        var resultArray = [];
        for(var i=min; i<=max;i++){
            if (i==string){
                resultArray.push(1);
            }
            else{
                resultArray.push(0);
            }
        }
        //console.log("min: "+min+" max: "+max);
        return resultArray;
    }

    render(){
        const{catcha, strings} = this.props;
        var min = this.getMinBund(catcha.fingerPoints);
        var max = this.getMaxBund(min);
        return(
            <React.Fragment>
                <div className="col-sm-10 col-md-10 col-lg-5 col-xl-5">
                <table cellSpacing="0" cellPadding="0">
                    <thead>
                    <tr>
                        <td>
                        </td>
                        <td>

                        </td>
                        {this.range(min,max).map(bund => {
                            return (
                                <td>{this.romanize(bund)}</td>)})}
                        </tr>
                    </thead>
                    <tbody>
                    {catcha.fingerPoints.map(string =>{
                        return(
                            <tr>
                                <td>
                                </td>
                                <td>
                                {
                                    (string.bund == -1) ?
                                        (<img src="view/nut_unplayed.png" height="20" width="60" max-width="12%"></img>) :
                                        ((string.bund==0)?
                                        (<img src="view/nut_open.png" height="20" width="60" max-width="12%"></img>):
                                            (<img src="view/nut_default.png" height="20" width="60" max-width="12%"></img>))
                                }
                                </td>
                                {
                                    this.getBunds(min, string.bund, max).map(bund => {
                                        var playedBundSrc = "view/bund_played_"+string.finger+".png";
                                        return (<td>
                                                    {
                                                        (bund == 1) ?
                                                            (<img src={playedBundSrc} height="20" width="60" max-width="12%"></img>) :
                                                            (<img src="view/bund_unplayed.png" height="20" width="60" max-width="12%"></img>)
                                                    }
                                                </td>);
                                    })
                                }
                                <td>{string.sound}</td>
                            </tr>
                        )
                    })}
                    </tbody>
                </table>
                <hr/>
                </div>
                <div className="col-sm-2 col-md-2 col-lg-1 col-xl-1 left">
                <ButtonDropDown favoritLists = {this.props.favoritLists}></ButtonDropDown>
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
        this.chordComponentURL = myURLs.getURL()+"chordComponents/";
        this.freeTextChordURL = myURLs.getURL()+"chordText/";
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

        favoritLists : [],
        strings: [],
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
        this.stringQuery();
        this.catchQuery();
    }

    isValid() {
        return (this.state.instrumental && this.state.instrumental.token != null && this.state.baseSound != null && this.state.baseType != null && this.state.chordType != null);
    }

    catchQuery(){
            if (this.isValid()) {
            let catchURL = myURLs.getURL() + "catch/" + this.state.instrumental.token + "/" + this.state.baseSound + "/" + this.state.baseType + "/" + this.state.chordType;
                axios.get(catchURL)
                    .then(res => {
                        console.log(res.data);
                        this.setState(
                            {
                                catches: res.data.catches,
                                bundDif: res.data.bundDif,
                                catchLoaded: true
                            });

                    }).catch(error => this.setState({error, catchLoaded: false}));
            }
    }
    stringQuery(){
        if (this.state.instrumental) {
            let stringURL = myURLs.getURL() + "strings/" + this.state.instrumental.token;
            axios.get(stringURL)
                .then(res => {
                    this.setState(
                        {
                            strings: res.data,
                            stringLoaded: true
                        });
                }).catch(error => this.setState({error, stringLoaded: false}));
        }
    }
    render() {
        const {catches, strings, catchLoaded, stringLoaded, bundDif, favoritLists} = this.state;
        return (
            <div>
                <ChordChooserList ancestor={this}></ChordChooserList>
                <div className="container">
                    <div className="row">
                        {( this.isValid()) ?
                            <ChordView catches={catches} strings={strings} catchLoaded={catchLoaded} stringLoaded={stringLoaded} bundDif = {bundDif} favoritLists = {favoritLists}></ChordView>
                            : (<h3> </h3>)}
                    </div>
                </div>
            </div>
        );
    }
}
export default ChordChooserPage;