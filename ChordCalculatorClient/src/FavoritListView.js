import React, { Component } from 'react'
import ModalDialogWithoutFooter from './Modal/ModalDialogWithoutFooter';
import CatchView from './CatchView';
import axios from 'axios';
import myURLs from './myURLs.js';
import jsPDF from 'jspdf';
import html2canvas from 'html2canvas';

export default class FavoritListView extends Component {
    title = "Lists"
    state = {show: false};

    componentDidMount(){
        this.setState({show : this.props.show});
    }
    showModal = () => {
        console.log(this.props);
        this.setState({show: true});
    };

    hideModal = () => {
        this.setState({show: false});
    };

    handleReject = () => {
        this.hideModal();
    };

    handleAccept = () => {
        this.props.onAccept();
        this.hideModal();
    };
    removeList(list){
        let removeListURL = myURLs.getURL() + "favorit/list/"+list.token;
        console.log(removeListURL);
        axios.delete(removeListURL).then(
            res=>{
                window.location.reload();
        });
    };
    generatePDF(list){
        html2canvas(document.getElementById(list.token+"_view")).then(canvas =>
            {
                let pdf = new jsPDF('p', 'mm', 'a4');
                pdf.addImage(canvas.toDataURL('image/png'), 'PNG', 0, 0, 211, 298);
                pdf.save(list.name+".pdf");
            });
    };
    removeCatch(catcha){
        let removeCatchURL = myURLs.getURL() + "favorit/catch/"+catcha.token;
        console.log(removeCatchURL);
        axios.delete(removeCatchURL).then(
            res=>{
                window.location.reload();
        });
    }
    render() {
        return (
            <div>
                <ModalDialogWithoutFooter title={this.title} show={this.state.show} handleReject={this.handleReject}>
                              {this.props.favoritLists != null ? 
                           (this.props.favoritLists.map(list=>
                                    <div class="row" id={list.token+"_view"}>
                                        <div>
                                            <p>{"List name: "+list.name}</p>
                                            <button onClick={()=>this.generatePDF(list)}><i class="fa fa-file-pdf-o"></i></button>
                                            <button onClick={()=>this.removeList(list)}><i class="fa fa-close"></i></button>
                                        </div>
                                        <div class="col-lg-4">
                                        {list.catches.map(catcha =>
                                            <CatchView catcha={catcha} view="list" bundDif={5} removeCatch={()=>this.removeCatch(catcha)}></CatchView>
                                            )
                                        }
                                        <br/>
                                        </div>
                                    </div>
                                )) : <h2>There is no list for this user.</h2>
                                }
                </ModalDialogWithoutFooter>
                <button className="btn btn-outline-secondary" onClick={this.showModal}>{this.title}</button>
            </div>
            )
    };
}