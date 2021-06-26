import React, { Component, useState } from "react";
import Form from "react-validation/build/form";
import Input from "react-validation/build/input";
import Button from "react-validation/build/button";
import CheckButton from "react-validation/build/button";
import {faFacebook} from "@fortawesome/free-brands-svg-icons";
import './logIn.css';

import AuthService from "../../services/authService";
import {FontAwesomeIcon} from "@fortawesome/react-fontawesome";


const required = value => {
    if (!value) {
        return (
            <div className="alert alert-danger" role="alert">
                This field is required!
            </div>
        );
    }
};


export default class Login extends Component {
    constructor(props) {
        super(props);
        this.handleLogin = this.handleLogin.bind(this);
        this.onChangeUsername = this.onChangeUsername.bind(this);
        this.onChangePassword = this.onChangePassword.bind(this);
        this.getFacebookAccessToken = this.getFacebookAccessToken.bind(this);

        this.state = {
            username: "",
            password: "",
            loading: false,
            message: ""
        };
    }

    getFacebookAccessToken() {
        var person;
        var ok = false;
        this.setState({loading: true});
        window.FB.login(
            function (response) {
                if (response.status === "connected") {
                    const facebookLoginRequest = {
                        accessToken: response.authResponse.accessToken,
                    };
                    AuthService.facebookLogin(facebookLoginRequest)
                        .then((response) => {
                            console.log(response);
                            localStorage.setItem("user", JSON.stringify(response.data));
                            person = response.data;
                            // this.props.history.push("/");
                            this.setState({loading: false});
                            ok = true;
                            console.log(ok);
                        })
                        .catch((error) => {
                            if (error.status === 401) {
                            } else {
                            }
                            //setFacebookLoading(false);
                        });
                } else {
                    console.log(response);
                }
            },
            {scope: "email"}
        );
        var user = localStorage.getItem("user");
        console.log(ok)
        if (user !== undefined) {
            this.props.setCurrentUser(user);
            this.props.props.history.push("/questions");
            setTimeout(function(){ window.location.reload();},5000);
        }
    };


    onChangeUsername(e) {
        this.setState({
            username: e.target.value
        });
    }

    onChangePassword(e) {
        this.setState({
            password: e.target.value
        });
    }

    handleLogin(e) {
        e.preventDefault();

        this.setState({
            message: "",
            loading: true
        });

        this.form.validateAll();

        if (this.checkBtn.context._errors.length === 0) {
            AuthService.logIn(this.state.username, this.state.password).then(
                (user) => {
                    console.log(user.data);
                    console.log(this.props);
                    this.props.setCurrentUser(user.data);
                    this.props.props.history.push("/questions");
                    //window.location.reload();
                    if (user.data.accessToken) {
                        localStorage.setItem("user", JSON.stringify(user.data));
                    }
                },
                error => {
                    const resMessage =
                        (error.response &&
                            error.response.data &&
                            error.response.data.message) ||
                        error.message ||
                        error.toString();

                    this.setState({
                        loading: false,
                        message: resMessage
                    });
                }
            );
        } else {
            this.setState({
                loading: false
            });
        }
    }

    render() {
        return (
            <div className="col-md-12">
                <div className="log-card card card-container">
                    <img
                        src="//ssl.gstatic.com/accounts/ui/avatar_2x.png"
                        alt="profile-img"
                        className="profile-img-card"
                    />

                    <Form
                        onSubmit={this.handleLogin}
                        ref={c => {
                            this.form = c;
                        }}
                    >
                        <div className="form-group">
                            <label className={"log-label"} htmlFor="username">Username</label>
                            <Input
                                type="text"
                                className="form-control"
                                name="username"
                                value={this.state.username}
                                onChange={this.onChangeUsername}
                                validations={[required]}
                            />
                        </div>

                        <div className="form-group">
                            <label className={"log-label"} htmlFor="password">Password</label>
                            <Input
                                type="password"
                                className="form-control"
                                name="password"
                                value={this.state.password}
                                onChange={this.onChangePassword}
                                validations={[required]}
                            />
                        </div>

                        <div className="form-group">
                            <button
                                className="btn btn-primary btn-block"
                                disabled={this.state.loading}
                            >
                                {this.state.loading && (
                                    <span className="spinner-border spinner-border-sm"></span>
                                )}
                                <span>Login</span>
                            </button>
                        </div>

                        {this.state.message && (
                            <div className="form-group">
                                <div className="alert alert-danger" role="alert">
                                    {this.state.message}
                                </div>
                            </div>
                        )}
                        <CheckButton
                            style={{ display: "none" }}
                            ref={c => {
                                this.checkBtn = c;
                            }}
                        />
                    </Form>
                    <button id={"fb-button"}
                            className="btn btn-outline-primary"
                            onClick={this.getFacebookAccessToken}
                    > <FontAwesomeIcon id="fb-icon" icon={faFacebook} className={"text-primary mr-2"}/>
                        Log in With Facebook
                    </button>
                </div>
            </div>
        );
    }
}