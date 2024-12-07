import React, { useState } from "react";
import ReactModal from "react-modal";
import ButtonGroup from "./ButtonGroup";
import PlaylistModal from "./PlaylistModal";
import "./Survey.css";
import axios from "axios";

const username = "Alex";

// Întrebările chestionarului fără ponderi
const questions = [
  "How excited are you about your future?",
  "How easy was it for you to smile today?",
  "How motivated are you to complete your daily tasks?",
  "How eager are you to start something new?",
];

// Funcție pentru generarea unui identificator unic scurt pentru numele playlistului
const generateShortId = () => Math.random().toString(16).slice(2, 6);

const Survey = ({
  show,
  setShowSurvey,
}: {
  show: boolean;
  setShowSurvey: Function;
}) => {
  const [currentQuestionIndex, setCurrentQuestionIndex] = useState(0);
  const [answers, setAnswers] = useState<number[]>([]);
  const [showPlaylist, setShowPlaylist] = useState(false);
  const [playlistName, setPlaylistName] = useState<string>("");
  const [loading, setLoading] = useState(false);

  // Funcție pentru gestionarea răspunsurilor utilizatorului
  const handleAnswer = (answer: number) => {
    const updatedAnswers = [...answers, answer];
    setAnswers(updatedAnswers);

    // Dacă este ultima întrebare, generează playlistul
    if (currentQuestionIndex === questions.length - 1) {
      handleGeneratePlaylist(updatedAnswers);
    }
    setCurrentQuestionIndex(currentQuestionIndex + 1);
  };

  // Funcție pentru generarea playlistului
  const handleGeneratePlaylist = async (answers: number[]) => {
    try {
      setLoading(true);

      // Generează un nume unic pentru playlist
      const generatedPlaylistName = `playlist_${username}_${generateShortId()}`;
      setPlaylistName(generatedPlaylistName);

      // Creează obiectul MoodDTO cu răspunsurile brute
      const moodDTO = {
        happiness_score: answers[0],
        sadness_score: answers[1],
        love_score: answers[2],
        energy_score: answers[3],
      };

      console.log("MoodDTO sent to back-end:", moodDTO);

      // Trimite cererea către back-end
      const response = await axios.post(
        `http://localhost:8080/api/userPlaylist/generate/${username}/${generatedPlaylistName}`,
        moodDTO
      );

      console.log("Playlist generated successfully:", response.data);
      setShowPlaylist(true);
      setLoading(false);
    } catch (error) {
      console.error("Error generating playlist:", error);
      setLoading(false);
    }
  };

  // Funcție pentru închiderea chestionarului
  const handleCloseSurvey = () => {
    setShowSurvey(false);
    setAnswers([]);
    setCurrentQuestionIndex(0);
  };

  const isLastQuestion = currentQuestionIndex >= questions.length;

  return (
    <>
      {/* Buton pentru deschiderea chestionarului */}
      <button onClick={() => setShowSurvey(true)} className="styleButton">
        Try now
      </button>

      {/* Fereastra modală a chestionarului */}
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
            {loading ? <p>Loading...</p> : null}
            <button className="create" onClick={() => setShowPlaylist(true)}>
              View Playlist
            </button>
            <button className="create" onClick={handleCloseSurvey}>
              Close Survey
            </button>
          </div>
        )}
      </ReactModal>

      {/* Fereastra modală pentru vizualizarea playlistului */}
      <PlaylistModal
        isOpen={showPlaylist}
        onRequestClose={() => setShowPlaylist(false)}
        playlistName={playlistName}
      />
    </>
  );
};

export default Survey;
