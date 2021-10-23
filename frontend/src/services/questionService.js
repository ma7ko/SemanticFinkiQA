import axios from '../custom-axios/axios';
import authHeader from "./auth-header";

const questionService = {
    getQuestions: () => {
        return axios.get("/questions",{ headers: authHeader() });
    },

    getQuestion: (URI) => {
        return axios.post("/questions/details", {
            "@type": "http://dbpedia.org/resource/Question",
            "@id": URI
        }, { headers: authHeader() });
    },

    addQuestion: (title, description, user, tags) => {
        console.log(title)
        console.log(description)
        console.log(user)
        console.log(tags)
            return axios.post("questions/add", {
                "@type": "http://dbpedia.org/resource/Question",
                "@context": {
                    "answer": "https://www.w3.org/Submission/sioc-spec/#term_has_reply",
                    "description": "s:description",
                    "dislikedBy": "http://purl.org/net/soron/dislikedBy",
                    "likedBy": "http://purl.org/net/soron/likedBy",
                    "s": "http://dbpedia.org/property/",
                    "tags": "s:tag",
                    "title": "s:title",
                    "writer": "s:writer"
                },
                "@id":`http://localhost:3000/${title}`,
                "title": title,
                "description": description,
                "likedBy":[],
                "dislikedBy": [],
                "writer": {
                    "@type": "http://dbpedia.org/resource/User",
                    "@id": user
                },
                "tags": tags,
                "prefix": "http://localhost:3000/questions/details/",
                "date": null,
                "answer": []
        }, { headers: authHeader() });
    },

    deleteQuestion: (URI) => {
        return axios.post("/questions/delete", {
            "@type": "http://dbpedia.org/resource/Question",
            "@id": URI
        }, { headers: authHeader() });
    },

    getAnswersByQuestion: (URI) => {
        return axios.post("/questions/answers", {
            "@type": "http://dbpedia.org/resource/Question",
            "@id": URI
        }, { headers: authHeader() });
    },

    likeQuestion: (question, user) => {
        console.log(user);
        return axios.post("/questions/like",
            {
                    "questionId": question["@id"],
                    "userId": user.id
                }, { headers: authHeader() })
    },

    dislikeQuestion: (question, user) => {
        return axios.post("/questions/dislike",
            {
                    "questionId": question["@id"],
                    "userId": user.id
                }, { headers: authHeader() })
    }
}

export default questionService;