import React, {Component} from 'react';
import Header from "../../Header/header";
import {BrowserRouter as Router, Redirect, Route, useParams, useRouteMatch} from "react-router-dom";
import QuestionList from "../QuestionList/questionList";
import QuestionsForm from "../QuestionForm/questionForm";
import QuestionDetails from "../QuestionDetails/questionDetails";
import Register from "../../Register/register";
import Login from "../../LogIn/logIn";
import Profile from "../../Profile/profile";
import QuestionService from "../../../services/questionService";


class QuestionRouting extends Component {


    constructor(props) {
        super(props);

        this.getQuestion = this.getQuestion.bind(this);

        this.state = {
            currentQuestion: {},
            path: "",
            url: ""
        }
    }

    getQuestion(questionId) {
        console.log("This is question id:")
        console.log(questionId);
        QuestionService.getQuestion(questionId).then((response) => {
            this.setState({
                currentQuestion: response.data
            });
        })
    }

    render() {
        return(
                        <div className={"container"}>
                        <Route path={`${this.props.match?.path}/list`} render={(props) => <QuestionList props={props} getQuestion={this.getQuestion} /> } />
                        <Route path={`${this.props.match?.path}/form/:id`} render={(props) => <QuestionsForm props={props} /> }/>
                        <Route path={`${this.props.match?.path}/details/:id`} render={(props) => <QuestionDetails props={props} question={this.state.currentQuestion} />}/>
                         </div>
);
    }

    componentDidMount() {
        console.log(this.props);

    }

}

export default QuestionRouting;