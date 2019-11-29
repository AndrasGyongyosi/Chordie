import React, { Component } from 'react'

export default class FavoritCatchView extends Component {
    render() {
        const {catcha} = this.props;
        return (
            <div>
                <p>{catcha.instrument +" "+catcha.chord}</p>
            </div>
            )
    };
}