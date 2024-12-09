import React, { useState } from "react";
import ReactModal from "react-modal";
import ButtonGroup from "./ButtonGroup";
import PlaylistModal from "./PlaylistModal";
import "./Survey.css";
import axios from "axios";

const username = "Alex";

// Întrebările chestionarului
const questions = [
  "How excited are you about your future?",
  "How easy was it for you to smile today?",
  "How motivated are you to complete your daily tasks?",
  "How eager are you to start something new?",
];

const generateShortId = () => Math.random().toString(16).slice(2, 6);

interface SurveyProps {
  show: boolean;
  setShowSurvey: (show: boolean) => void;
}

const Survey: React.FC<SurveyProps> = ({ show, setShowSurvey }) => {
  const [currentQuestionIndex, setCurrentQuestionIndex] = useState(0);
  const [answers, setAnswers] = useState<number[]>([]);
  const [showPlaylist, setShowPlaylist] = useState(false);
  const [playlistName, setPlaylistName] = useState<string>("");
  const [loading, setLoading] = useState(false);
  const [errorMessage, setErrorMessage] = useState<string | null>(null);

  const handleAnswer = (answer: number) => {
    const updatedAnswers = [...answers, answer];
    setAnswers(updatedAnswers);

    if (currentQuestionIndex === questions.length - 1) {
      handleGeneratePlaylist(updatedAnswers);
    }
    setCurrentQuestionIndex(currentQuestionIndex + 1);
  };

  const handleGeneratePlaylist = async (answers: number[]) => {
    setLoading(true);
    setErrorMessage(null);

    const token = localStorage.getItem("token");
    if (!token) {
      // Utilizatorul nu este autentificat
      setErrorMessage("You must be logged in to generate a playlist.");
      setLoading(false);
      return;
    }

    try {
      const generatedPlaylistName = `playlist_${username}_${generateShortId()}`;
      setPlaylistName(generatedPlaylistName);

      const moodDTO = {
        happiness_score: answers[0],
        sadness_score: answers[1],
        love_score: answers[2],
        energy_score: answers[3],
      };

      console.log("MoodDTO sent to back-end:", moodDTO);

      const response = await axios.post(
        `http://localhost:8080/api/userPlaylist/generate/${username}/${generatedPlaylistName}`,
        moodDTO,
        {
          headers: {
            Authorization: `Bearer ${token}`,
          },
        }
      );

      console.log("Playlist generated successfully:", response.data);
      setShowPlaylist(true);
      setLoading(false);
    } catch (error: any) {
      console.error("Error generating playlist:", error);
      setErrorMessage("Failed to generate playlist. Please try again.");
      setLoading(false);
    }
  };

  const handleCloseSurvey = () => {
    setShowSurvey(false);
    setAnswers([]);
    setCurrentQuestionIndex(0);
    setErrorMessage(null);
  };

  const isLastQuestion = currentQuestionIndex >= questions.length;

  return (
    <>
      <button onClick={() => setShowSurvey(true)} className="styleButton">
        Try now
      </button>

      <ReactModal
        isOpen={show}
        onRequestClose={handleCloseSurvey}
        contentLabel="Survey"
        overlayClassName="customOverlay"
        className="customModal"
      >
        {!isLastQuestion ? (
          <div className="question-container">
            <h2>{questions[currentQuestionIndex]}</h2>
            <ButtonGroup onAnswer={handleAnswer} />
          </div>
        ) : (
          <div className="summary">
            <h2>Thank you for completing the survey!</h2>
            {loading && <p>Loading...</p>}
            {errorMessage && <p style={{ color: "red" }}>{errorMessage}</p>}
            {!loading && !errorMessage && (
              <button className="create" onClick={() => setShowPlaylist(true)}>
                View Playlist
              </button>
            )}
            <button className="create" onClick={handleCloseSurvey}>
              Close Survey
            </button>
          </div>
        )}
      </ReactModal>

      <PlaylistModal
        isOpen={showPlaylist}
        onRequestClose={() => setShowPlaylist(false)}
        playlistName={playlistName}
      />
    </>
  );
};

export default Survey;
