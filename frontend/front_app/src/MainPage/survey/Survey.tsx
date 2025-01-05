import React, { useState } from "react";
import ReactModal from "react-modal";
import ButtonGroup from "./ButtonGroup";
import PlaylistModal from "./PlaylistModal";
import "./Survey.css";
import axios from "axios";

// Întrebările chestionarului
const questions = [
  "How much do you feel like smiling today?",
  "How often did you feel down or upset today?",
  "How strongly do you feel connected or caring towards others right now?",
  "How motivated do you feel to perform physical or mental tasks today?",
];

// Funcție simplă pentru a genera un ID scurt
const generateShortId = () => Math.random().toString(16).slice(2, 6);

interface SurveyProps {
  show: boolean;
  setShowSurvey: (show: boolean) => void;
}

const Survey: React.FC<SurveyProps> = ({ show, setShowSurvey }) => {
  // Indexul întrebării curente
  const [currentQuestionIndex, setCurrentQuestionIndex] = useState(0);

  // Răspunsurile date de user
  const [answers, setAnswers] = useState<number[]>([]);

  // Stare pentru a deschide/închide modalul cu playlist
  const [showPlaylist, setShowPlaylist] = useState(false);

  // Numele playlist-ului generat
  const [playlistName, setPlaylistName] = useState<string>("");

  // Stare de încărcare
  const [loading, setLoading] = useState(false);

  // Mesaj de eroare
  const [errorMessage, setErrorMessage] = useState<string | null>(null);
  const [playlistLink, setPlaylistLink] = useState<string>(""); // Asigură-te că e un string, nu `null`



  // Funcție apelată când userul alege un răspuns la întrebare
  const handleAnswer = (answer: number) => {
    const updatedAnswers = [...answers, answer];
    setAnswers(updatedAnswers);

    if (currentQuestionIndex === questions.length - 1) {
      // Dacă e ultima întrebare, generează playlist
      handleGeneratePlaylist(updatedAnswers);
    }
    setCurrentQuestionIndex(currentQuestionIndex + 1);
  };

  // Funcție care trimite cererea de generare a playlist-ului către backend (Spotify)
  // După ce ai obținut răspunsul din backend, transmiți playlistLink la PlaylistModal
  interface MoodDTO {
    happinessScore: number;
    sadnessScore: number;
    loveScore: number;
    energyScore: number;
  }

  const handleGeneratePlaylist = async (answers: number[]): Promise<void> => {
    setLoading(true);
    setErrorMessage(null);

    const token = localStorage.getItem("token");
    const username = localStorage.getItem("username");

    if (!token || !username) {
      setErrorMessage("You must be logged in to generate a playlist.");
      setLoading(false);
      return;
    }

    try {
      const generatedPlaylistName = `playlist_${username}_${generateShortId()}`;
      setPlaylistName(generatedPlaylistName);

      const moodDTO: MoodDTO = {
        happinessScore: answers[0],
        sadnessScore: answers[1],
        loveScore: answers[2],
        energyScore: answers[3],
      };

      const url = `http://localhost:8080/api/spotify/generate-playlist?username=${username}&playlistName=${generatedPlaylistName}&happinessScore=${moodDTO.happinessScore}&sadnessScore=${moodDTO.sadnessScore}&loveScore=${moodDTO.loveScore}&energyScore=${moodDTO.energyScore}`;

      const response = await axios.post<string>(url, null, {
        headers: {
          Authorization: `Bearer ${token}`,
        },
      });

      console.log("Playlist generated successfully:", response.data);

      const playlistLink = response.data; // Asigură-te că este un string valid din răspunsul backend-ului
      setPlaylistLink(playlistLink); // Salvează link-ul
      setShowPlaylist(true); // Deschide modalul pentru playlist
      
    } catch (error) {
      console.error("Error generating playlist:", error);
      setErrorMessage("Failed to generate playlist. Please try again.");
    } finally {
      setLoading(false);
    }
  };

  // Survey.tsx
const handlePlayPlaylist = async () => {
  setLoading(true);
  const token = localStorage.getItem("token");
  const username = localStorage.getItem("username");

  if (!token || !username) {
    setErrorMessage("You must be logged in to play a playlist.");
    setLoading(false);
    return;
  }

  try {
    const url = `http://localhost:8080/api/spotify/play-playlist?username=${username}&playlistId=${playlistLink}`;

    // Trimiterea cererii pentru a reda playlist-ul
    await axios.post(url, null, {
      headers: {
        Authorization: `Bearer ${token}`,
      },
    });

    setLoading(false);
  } catch (error) {
    console.error("Error playing playlist:", error);
    //setErrorMessage("Failed to play playlist. Please try again.");
    setLoading(false);
  }
};

  
  // Închide Survey-ul
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
        {/* Dacă nu s-a ajuns la ultima întrebare, arată întrebarea curentă */}
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
        playlistLink={playlistLink}
      />
    </>
  );
};

export default Survey;
