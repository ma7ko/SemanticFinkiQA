import axios from '../custom-axios/axios';
import authHeader from "./auth-header";

const answerService = {
    getAnswers: () => {
        return axios.get("/answers", { headers: authHeader() });
    },

    getAnswer: (URI) => {
        return axios.get(`/answers/${URI}`, { headers: authHeader() });
    },

    addAnswer: (explanation, likes, dislikes, questionId, username) => {
        return axios.post("/answers/add", {
            "@type": "http://dbpedia.org/resource/Answer",
            "@context": {
                "date": "s:date",
                "dislikedBy": "http://purl.org/net/soron/dislikedBy",
                "explanation": "s:explanation",
                "likedBy": "http://purl.org/net/soron/likedBy",
                "question": "https://www.w3.org/Submission/sioc-spec/#term_reply_of",
                "s": "http://dbpedia.org/property/",
                "title": "s:title",
                "writer": "s:writer"
            },
            "@id": "http://localhost:3000/",
            "explanation": explanation,
            "likedBy":[],
            "dislikedBy": [],
            "question": questionId,
            "prefix": "http://localhost:3000/answers/details/",
            "writer": {
                "@type": "http://dbpedia.org/resource/User",
                "@id": username
            }
        }, { headers: authHeader() });
    },

    deleteAnswer: (URI) => {
        return axios.post(`/answers/delete`,{
            "@id": URI,
        }, { headers: authHeader() });
    },

    likeAnswer: (answer, user) => {
        console.log(user);
        return axios.post("/answers/like",
            {
                "questionId": answer["@id"],
                "userId": user.id
            }, { headers: authHeader() })
    },

    dislikeAnswer: (answer, user) => {
        return axios.post("/answers/dislike",
            {
                "questionId": answer["@id"],
                "userId": user.id
            }, { headers: authHeader() })
    }
}

export default answerService;