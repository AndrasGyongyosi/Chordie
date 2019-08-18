import React from 'react';
import './App.css';
import axios from "axios";

const url= 'http://localhost:8080/'

class Instrumentals extends React.Component {
    state={instrumentals : [],
            loaded : false,
            error: null,
            showModal: false,
    };
    componentDidMount() {
        let instrumentalURL = url + "instrumental/" + this.props.chordChooserList.ancestor.props.user;
        //console.log("instrumentalURL: "+instrumentalURL);
        axios.get(instrumentalURL)
            .then(res => {
                console.log(res.data);
                this.setState(
                    {
                        instruments: res.data,
                        loaded: true,
                    });
                this.setInstrumental(res.data[0]);
            }).catch(error => this.setState({error, loaded: false}));
    }
    setInstrumental(instrumental){
        this.props.chordChooserList.instrumentalChange(instrumental.name);
    }
    newInstrumental(){
        console.log("new Instrumental.");
        this.setState({'showModal':true});
        //document.getElementById("exampleModal").modal('show');

    }
    render(){
        const {loaded, instrumentals} = this.state;
        return (<React.Fragment>
                    <nav className="navbar navbar-expand-lg navbar-light bg-light">
                    <h1 className="fas fa-guitar"> Instumentals </h1>
                    {((loaded) ? (
                        //<ul className="list-group">
                            {instruments.map(
                        instrumental =>{
                            return (<li onClick={() =>this.setInstrumental(instrumental)} className="list-group-item" data-toggle="list" role="tab">{instrumental.name}</li>)
                            })}
                            <li onClick={() =>this.newInstrumental()}className="list-group-item" data-toggle="list" role="tab">New Instrumental</li>
                        //</ul>
                        /*<Modal
                        isOpen={this.state.modalIsOpen}
                        onAfterOpen={this.afterOpenModal}
                        onRequestClose={this.closeModal}
                        style={customStyles}
                        contentLabel="Example Modal"
                        >

                        <h2 ref={subtitle => this.subtitle = subtitle}>Hello</h2>
                        <button onClick={this.closeModal}>close</button>
                        <div>I am a modal</div>
                        <form>
                        <input />
                        <button>tab navigation</button>
                        <button>stays</button>
                        <button>inside</button>
                        <button>the modal</button>
                        </form>
                        </Modal>*/
                        ) : (<h2>Loading...</h2>))}
                    </nav>
                </React.Fragment>
        );
    }
}

export default Instrumentals;