import React, { Component } from 'react'
import ModalDialogWithoutFooter from './Modal/ModalDialogWithoutFooter';
import CatchView from './CatchView';
import axios from 'axios';
import myURLs from './myURLs.js';
import jsPDF from 'jspdf';
import html2canvas from 'html2canvas';
import ModalDialog from "./Modal/ModalDialog"
import ReactDOM from 'react-dom'

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
    
    render() {
        return (
            <div>
                <ModalDialogWithoutFooter title={this.title} show={this.state.show} handleReject={this.handleReject}>
                            {this.props.favoritLists != null ? 
                            this.props.favoritLists.map(list=> <CatchList list={list}/>) : 
                            <h2>There is no list for this user.</h2>
                                }
                            <NewCatchList userToken={this.props.userToken}></NewCatchList>
                </ModalDialogWithoutFooter>
                <button className="btn btn-outline-secondary" onClick={this.showModal}>{this.title}</button>
            </div>
            )
    };
}

class NewCatchList extends Component{
    state={
        show:false,
    }
    showModal(){
        this.setState({
            show:true
        });
    }

    handleAccept = () => {
        let newListURL = myURLs.getURL() + "list/new"; 
        let passedParameters = {
          "name": document.getElementById("listName").value,
          "userToken": this.props.userToken,
          "catches": []
      };
      axios.post(newListURL, passedParameters)
          .then(res=>{
              this.setState({show:false});
              window.location.reload();
      });
      }
      handleReject = () => {
        console.log("new List Rejected");
        this.setState({show:false});
      }

    render(){
        return(
            <div>
                <button className="right" onClick={()=>this.showModal()}><i class="fa fa-plus-square"></i></button>
                <ModalDialog title="Add" show={this.state.show} handleAccept={this.handleAccept} handleReject={this.handleReject} >
                    <div className="container">
                        <div className="row">
                        <div className="col-lg-6">
                            <label htmlFor="listName">List name</label>
                            <input type="text" className="form-control" id="listName" placeholder="List name" autofocus="true" defaultValue=""/>
                        </div>
                        </div>
                    </div>
                </ModalDialog>
            </div>
        );
    }
}

class CatchList extends Component{
    state = {
        show : false
    }

    removeList(){
        let removeListURL = myURLs.getURL() + "list/delete/"+this.props.list.listToken;
        console.log(removeListURL);
        axios.delete(removeListURL).then(
            res=>{
                window.location.reload();
        });
    };
    generatePDF(){
        let pdf = new jsPDF('p', 'mm', 'a4');
        let height = 10;
        let newrow=true;
        let html = document.querySelector("#pdf");
        console.log(html); 
        let header = document.createElement("div");
        //header.id = "header";
        header.className="col-lg-12";
        ReactDOM.render(( <h2 className= "centered">{this.props.list.name}</h2>),header);
        html.appendChild(header);
        console.log(header);
        let row;
        let catchHtml
        this.props.list.catches.map(catcha => {
            if (newrow){
                row = document.createElement("div");
                row.className="row";
                newrow=false;
            } else {
                newrow=true;
            }
            catchHtml = document.createElement("div");
            catchHtml.className="col-lg-6";
            ReactDOM.render((
                <CatchView catcha={catcha} view="print" bundDif={4} removeCatch={()=>this.removeCatch(catcha)}></CatchView>), catchHtml);
            row.appendChild(catchHtml);
            if (newrow){
                html.appendChild(row);
                height+=60;
            }
        });
        if (!newrow){
            html.appendChild(row);
            height+=60;
        }
        console.log(html);
        html2canvas(html).then(canvas =>
            {
                pdf.addImage(canvas.toDataURL('image/png'), 'PNG', 0, 0, 200, height);
                pdf.save(this.props.list.name+".pdf");
                ReactDOM.render(<p></p>,html);
            });
    };
    removeCatch(catcha){
        let removeCatchURL = myURLs.getURL() + "list/deleteCatch/"+catcha.catchToken;
        console.log(removeCatchURL);
        axios.delete(removeCatchURL).then(
            res=>{
                window.location.reload();
        });
    }
    showDetails(){
        this.setState({show : true});
    }
    hideDetails(){
        this.setState({show : false});
    }

    render() {
        const {list} = this.props;
        return ( 
        <div class="container catchlist" id={list.listToken+"_view"}>
            <div class="catchlistheader row">
                <h2 class="inline col-lg-4">{list.name}</h2>
                <p class="inline col-lg-4">{"size : "+list.catches.length}</p>
                <div className="inline col-lg-4">
                    <button onClick={()=>this.generatePDF()}><i class="fa fa-file-pdf-o"></i></button>
                    <button onClick={()=>this.removeList()}><i class="fa fa-close"></i></button>
                    {(this.state.show ? 
                    (<button onClick={()=>this.hideDetails()}><i class="fa fa-hand-o-up"></i></button>):
                    (<button onClick={()=>this.showDetails()}><i class="fa fa-hand-o-down"></i></button>)
                    )}
                </div>
            </div>
            {(this.state.show ? (
            <div>
                {list.catches.map(catcha =>
                <div className="catchlistbody row">
                    <div className="col-lg-2"/>
                    <div className="col-lg-8">
                    <hr/>
                    <CatchView catcha={catcha} view="list" bundDif={5} removeCatch={()=>this.removeCatch(catcha)}></CatchView>
                    </div>
                    <div className="col-lg-2"/>
                </div>
                    )
                }
            <br/>
            </div>
            ) : (<div></div>))
                        }
        <div id="pdf" className="container"></div>
        </div>
    )};
}