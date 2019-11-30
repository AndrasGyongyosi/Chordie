import React, { Component } from 'react'
import ModalDialogWithoutFooter from './Modal/ModalDialogWithoutFooter';
import CatchView from './CatchView';
import axios from 'axios';
import myURLs from './myURLs.js';

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
                                    <div class="row">
                                        <div>
                                            <p>{"List name: "+list.name}</p>
                                        </div>
                                        <div>
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