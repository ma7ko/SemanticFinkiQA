import React from 'react';
import {Link} from "react-router-dom";
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome'
import {faSignInAlt, faSignOutAlt, faUser} from "@fortawesome/free-solid-svg-icons";

const header = (props) => {
    return (
        <nav className="navbar navbar-expand-lg navbar-light bg-light">
            <button className="navbar-toggler collapsed" type="button" data-toggle="collapse" data-target="#navbarToggler"
                    aria-controls="navbarToggler" aria-expanded="false" aria-label="Toggle navigation">
                <span className="navbar-toggler-icon"></span>
            </button>
            <Link className="navbar-brand" to={"/questions"}>Finki Q&A</Link>

            <div className="collapse navbar-collapse" id="navbarToggler">
                <ul className="navbar-nav mr-auto mt-2 mt-lg-0">
                    <li className="nav-item">
                        <Link className="nav-link" to={"/questions"}>Home</Link>
                    </li>
                    {
                        props.currentUser?.username !== undefined &&
                        <li className="nav-item">
                            <Link className="nav-link" onClick={() => {props.resetCurrentQuestion()}} to={`/questions/form/new`}>Ask question</Link>
                        </li>
                    }
                    {
                        props.currentUser?.username === undefined &&
                        <li className="nav-item">
                            <Link className="nav-link" to={`/questions/form/new`}>Ask question</Link>
                        </li>
                    }
                    {
                        props.currentUser?.roles?.includes("ROLE_ADMIN") &&
                        <li className="nav-item">
                            <Link className="nav-link" to={"/tags"}>Tags</Link>
                        </li>
                    }
                </ul>
                <ul className="navbar-nav mt-2 mt-lg-0 float-right">
                    {
                        props.currentUser?.username === undefined &&
                        <li className="nav-item">
                            <Link className="nav-link" to={"/login"}>Log in<FontAwesomeIcon className={'ml-2'} icon={faSignInAlt}/></Link>
                        </li>
                    }
                    {
                        props.currentUser?.username === undefined &&
                        <li className="nav-item">
                            <Link className="nav-link" to={"/register"}>Register</Link>
                        </li>
                    }
                    {
                        props.currentUser?.username !== undefined &&
                        <li className="nav-item">
                            <Link className="nav-link" onClick={() => {props.setProfileUser(props.currentUser)}} to={`/profile/${props.currentUser?.uri}`}>{props.currentUser?.username}<FontAwesomeIcon className={'ml-2'} icon={faUser}/></Link>
                        </li>
                    }
                    {
                        props.currentUser?.username !== undefined &&
                        <li className="nav-item">
                            <Link to={"/questions"} className="nav-link" onClick={() => {props.logOut(); window.location.reload();}}>LogOut<FontAwesomeIcon className={'ml-2'} icon={faSignOutAlt}/></Link>
                        </li>
                    }
                </ul>
                {/*<form className="form-inline my-2 my-lg-0">*/}
                {/*    <input className="form-control mr-sm-2" type="search" placeholder="Search" aria-label="Search"/>*/}
                {/*        <button className="btn btn-outline-success my-2 my-sm-0" type="submit">Search</button>*/}
                {/*</form>*/}
            </div>

        </nav>
    );
}

export default header;