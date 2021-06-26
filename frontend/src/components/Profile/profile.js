import React, { Component } from "react";
import AuthService from "../../services/authService";
import {Link} from 'react-router-dom';

export default class Profile extends Component {
    constructor(props) {
        super(props);

        this.state = {
            currentUser: [JSON.parse(localStorage.getItem('user'))]
        };
    }

    render() {
        const { currentUser } = this.state;

        return (
            <div className="container">
                <header className="jumbotron">
                    <h3>
                        <div>{console.log(this.props)}</div>
                        <div>{console.log(this.state.currentUser)}</div>
                        <strong>{this.props.currentUser?.username}</strong> Profile
                    </h3>
                </header>
                <p>
                    <strong>Email:</strong>{" "}
                    {this.props.currentUser?.email}
                </p>
                <strong>Authorities:</strong>
                <ul>
                    {this.props.currentUser?.roles &&
                    this.props.currentUser?.roles.map((role, index) => <li key={index}>{role}</li>)}
                </ul>
                <Link className={"btn btn-primary"} onClick={() => {this.props.props.history.goBack()}}>Back</Link>
            </div>
        );
    }
}