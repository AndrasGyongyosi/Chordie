import React from 'react';
class CatchList extends React.Component {
    render (){
    return(
    <React.Fragment>
        <button className="minibutton">{this.props.listName}</button><br/>
    </React.Fragment>);
    }
}
export default CatchList