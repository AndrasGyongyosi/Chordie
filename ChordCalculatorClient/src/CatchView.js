import React from 'react';
import ButtonDropDown from './ButtonDropDown';

class CatchView extends React.Component{
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
        // view -> page, list or print
        const{catcha, favoritLists, token, instrument, view} = this.props;
        var min = this.getMinBund(catcha.stringCatches);
        var max = this.getMaxBund(min);
        return(
            <React.Fragment>
                <div className={view=="page" ? "col-sm-10 col-md-10 col-lg-5 col-xl-5" : "col-sm-10 col-md-10 col-lg-10 col-xl-10"}>
                <div className="row">
                    <p className="col-lg-11 inline" hidden={view=="page"}>{catcha.chord+" on "+catcha.instrument}</p>
                    
                    <button className="col-lg-1 btn" onClick={this.props.removeCatch} hidden={view!="list"}><i class="fa fa-close"></i></button>
                </div>
                <div class="row">
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
                    {catcha.stringCatches.map(string =>{
                        return(
                            <tr>
                                <td>
                                </td>
                                <td>
                                {
                                    (string.bund == -1) ?
                                        (<img src={"view/nut_unplayed.png"} height={this.props.view=="print" ? "15" : "20"} width={this.props.view=="print" ? "40" : "60"} max-width="12%"></img>) :
                                        ((string.bund==0)?
                                        (<img src={"view/nut_open.png"} height={this.props.view=="print" ? "15" : "20"} width={this.props.view=="print" ? "40" : "60"} max-width="12%"></img>):
                                            (<img src={"view/nut_default.png"} height={this.props.view=="print" ? "15" : "20"} width={this.props.view=="print" ? "40" : "60"} max-width="12%"></img>))
                                }
                                </td>
                                {
                                    this.getBunds(min, string.bund, max).map(bund => {
                                        var playedBundSrc = "view/bund_played_"+string.finger+".png";
                                        return (<td>
                                                    {
                                                        (bund == 1) ?
                                                            (<img src={playedBundSrc} height={this.props.view=="print" ? "15" : "20"} width={this.props.view=="print" ? "40" : "60"} max-width="12%"></img>) :
                                                            (<img src={"view/bund_unplayed.png"} height={this.props.view=="print" ? "15" : "20"} width={this.props.view=="print" ? "40" : "60"} max-width="12%"></img>)
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
                </div>
                <hr/>
                </div>
                <div className="col-sm-2 col-md-2 col-lg-1 col-xl-1 left" hidden={token==undefined}>
                    <ButtonDropDown catch={this} favoritLists = {favoritLists} token = {token} instrument={instrument}></ButtonDropDown>
                </div>
            </React.Fragment>
        )
    }
}
export default CatchView;