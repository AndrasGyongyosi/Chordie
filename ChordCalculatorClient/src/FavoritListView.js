import React, { Component } from 'react'
import axios from "axios";
import myURLs from './myURLs.js';
import ModalDialogWithoutFooter from './Modal/ModalDialogWithoutFooter';

export default class FavoritListView extends Component {
    title = "Lists"
    state = {show: false};

    componentDidMount(){
        this.setState({show : this.props.show});
    }
    showModal = () => {
        this.setState({show: true});
    };

    hideModal = () => {
        this.setState({show: false});
    };

    handleReject = () => {
        this.hideModal();
    };

    handleAccept = () => {
        console.log(this.props);
        this.props.onAccept();
        this.hideModal();
    };
    render() {
        return (
            <div>
                <ModalDialogWithoutFooter title={this.title} show={this.state.show} handleReject={this.handleReject}>
                                {this.props.favoritLists != null ? 
                                (this.props.favoritLists.map(list=>{
                                    return(<h2>{list.name}</h2>)
                                })) : <h2>There is no list for this user.</h2>
                                }
                </ModalDialogWithoutFooter>
                <button className="btn btn-outline-secondary" onClick={this.showModal}>{this.title}</button>
            </div>
            )
    };
}