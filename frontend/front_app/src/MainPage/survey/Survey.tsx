import React, { useState } from "react";
import ReactModal from "react-modal";
import ButtonGroup from "./ButtonGroup";
import PlaylistModal from "./PlaylistModal";
import "./Survey.css";
import axios from "axios";

/**
 * @fileoverview Survey component for collecting user mood data and generating playlists
 * Provides a series of questions, collects user responses, and interacts with the backend API
 * @requires react
 * @requires react-modal
 * @requires ./ButtonGroup
 * @requires ./PlaylistModal
 * @requires ./Survey.css
 * @requires axios
 */

// Întrebările chestionarului
const questions = [
  "How much do you feel like smiling today?",
  "How often did you feel down or upset today?",
  "How strongly do you feel connected or caring towards others right now?",
  "How motivated do you feel to perform physical or mental tasks today?",
];

let mood = "boo";

// Funcție simplă pentru a genera un ID scurt
const generateShortId = () => Math.random().toString(16).slice(2, 6);

interface SurveyProps {
  show: boolean;
  setShowSurvey: (show: boolean) => void;
  textButton: string;
  styleButton: string;
}

/**
 * Survey component collects user mood data through a series of questions
 * Generates a playlist based on the user's responses by interacting with the backend API
 *
 * @component
 * @param {Object} props - Component properties
 * @param {boolean} props.show - Flag to control the visibility of the survey modal
 * @param {Function} props.setShowSurvey - Function to update the visibility of the survey modal
 * @param {string} props.textButton - Text to display on the survey trigger button
 * @param {string} props.styleButton - CSS class name for the survey trigger button
 * @returns {JSX.Element} Renders the survey modal and playlist modal
 */ 
const Survey: React.FC<SurveyProps> = ({
  show,
  setShowSurvey,
  textButton,
  styleButton,
}) => {
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

  /**
   * Event handler for user answering a question
   * Updates the answers state and advances to the next question or generates the playlist
   *
   * @param {number} answer - User's answer to the current question
   */
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

  /**
   * Generates a playlist based on the user's survey responses
   * Sends a request to the backend API to create the playlist on Spotify
   *
   * @async
   * @param {number[]} answers - User's answers to the survey questions
   */
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

      let nrMood = 0;
      if (moodDTO.happinessScore > nrMood) {
        mood = "happy";
        nrMood = moodDTO.happinessScore;
      }
      if (moodDTO.sadnessScore > nrMood) {
        mood = "sad";
        nrMood = moodDTO.sadnessScore;
      }
      if (moodDTO.loveScore > nrMood) {
        mood = "love";
        nrMood = moodDTO.loveScore;
      }
      if (moodDTO.energyScore > nrMood) {
        mood = "energy";
        nrMood = moodDTO.energyScore;
      }

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
  /**
   * Starts playing the generated playlist on Spotify
   *
   * @async
   */
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

  /**
   * Closes the survey modal and resets the component state
   */
  const handleCloseSurvey = () => {
    setShowSurvey(false);
    setAnswers([]);
    setCurrentQuestionIndex(0);
    setErrorMessage(null);
  };

  const isLastQuestion = currentQuestionIndex >= questions.length;

  return (
    <>
      <button onClick={() => setShowSurvey(true)} className={styleButton}>
        {textButton}
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
            <div className="button-container">
              {!loading && !errorMessage && (
                <button
                  className="create"
                  onClick={() => setShowPlaylist(true)}
                >
                  View Playlist
                </button>
              )}
              <button className="create-close" onClick={handleCloseSurvey}>
                Close Survey
              </button>
            </div>
          </div>
        )}
      </ReactModal>

      <PlaylistModal
        isOpen={showPlaylist}
        onRequestClose={() => setShowPlaylist(false)}
        playlistName={playlistName}
        playlistLink={playlistLink}
        mood={mood}
        playlistSpotifyLink={playlistLink}
      />
    </>
  );
};

export default Survey;