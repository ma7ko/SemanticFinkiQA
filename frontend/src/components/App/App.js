import './App.css';
import 'bootstrap/dist/css/bootstrap.min.css';
import QuestionService from '../../services/questionService';
import {Component} from "react";
import QuestionsForm from "../Question/QuestionForm/questionForm";
import React from 'react';
import {BrowserRouter as Router, Redirect, Route, useParams, useRouteMatch} from 'react-router-dom';
import QuestionList from "../Question/QuestionList/questionList";
import Header from '../Header/header';
import QuestionDetails from "../Question/QuestionDetails/questionDetails";
import Register from "../Register/register";
import Login from "../LogIn/logIn";
import Profile from "../Profile/profile";
import QuestionRouting from "../Question/QuestionRouting/questionRouting";
import AuthService from "../../services/authService";


class App extends Component {

    constructor(props) {
        super(props);

        this.logOut = this.logOut.bind(this);
        this.getQuestion = this.getQuestion.bind(this);
        this.resetCurrentQuestion = this.resetCurrentQuestion.bind(this);

        this.state = {
            questions: [],
            answers: [],
            isAdd: false,
            currentUser: {},
            currentQuestion: {},
            selectedUser: {},
            latestQuestionToEdit: {},
            questionId: ""
        }
    }

    getQuestion(questionId) {
        this.setState({
            questionId: questionId
        })
        console.log("This is question id:")
        console.log(questionId);
        QuestionService.getQuestion(questionId).then((response) => {
            this.setState({
                currentQuestion: response.data,
                latestQuestionToEdit: response.data
            });
        })
    }

    resetCurrentQuestion() {
        this.setState({
            currentQuestion: {},
            questionId: ""
        })
    }

    render() {
        return (
            <Router>
                <Header logOut={this.logOut} currentUser={this.state.currentUser} setProfileUser={this.setProfileUser} resetCurrentQuestion={this.resetCurrentQuestion}/>
                <main>
                <div className={"container"}>
                    <Route path={"/questions"} exact render={(props) => <QuestionList props={props} getQuestion={this.getQuestion} resetCurrentQuestion={this.resetCurrentQuestion} setProfileUser={this.setProfileUser}  currentUser={this.state.currentUser}/> } />
                    <Route path={"/questions/form/:id"} exact render={(props) => <QuestionsForm props={props} questionId={this.state.questionId} latestQuestionToEdit={this.state.latestQuestionToEdit} currentUser={this.state.currentUser} /> }/>
                    <Route path={"/questions/details/:id"} exact render={(props) => <QuestionDetails props={props} question={this.state.currentQuestion} currentUser={this.state.currentUser} getQuestion={this.getQuestion} setProfileUser={this.setProfileUser}/>}/>
                    <Route path={"/register"} exact render={(props) => <Register props={props} /> }/>
                    <Route path={"/login"} exact render={(props) =>
                        <Login setCurrentUser={this.setCurrentUser} props={props}/> } />
                    <Route path={"/profile/:id"} exact render={(props) =>
                        <Profile props={props} currentUser={this.state.selectedUser}/> } />
                    <Redirect to={"/questions"}/>
                </div>
            </main>

        </Router>
        );
    }

    setCurrentUser = (user) => {
        this.setState({
            currentUser: user
        });
    }

    getCurrentUser = () => {
        let user = localStorage.getItem('user');
        return JSON.parse(user);
    }

    setProfileUser = (user) => {
        this.setState({
            selectedUser: user
        })
    }

    logOut() {
        AuthService.logOut();
    }

    componentDidMount() {
        const user = this.getCurrentUser();

        if (user) {
            this.setCurrentUser(user);
        }
    }

}

export default App;
