/**
 * @fileoverview About component that displays the application's mission and description
 * @requires ./About.css
 */

import "./About.css";

/**
 * About component that displays information about Zenith's mission and features
 * Presents a description of the platform's music personalization capabilities
 * and its approach to mood-based playlist generation
 * 
 * @component
 * @returns {JSX.Element} A styled div containing three paragraphs of content about Zenith
 */
const About = () => {
  return (
    <div className="box">
      <p>
        Welcome to Zenith, where your emotions set the tone for the perfect
        playlist. We believe music is more than just sound; it's a companion, a
        motivator, and a storyteller that resonates with your unique mood.{" "}
      </p>

      <p>
        At Zenith, we've created a seamless experience to help you uncover the
        soundtrack of your day. Through our intuitive survey, we get to know you
        better—your feelings, vibes, and energy—and transform that into a
        tailor-made playlist that's as dynamic as you are. Whether you're
        feeling joyful, contemplative, or ready to take on the world, we've got
        the music to match.{" "}
      </p>

      <p>
        Our mission is simple: to enhance your everyday moments through the
        power of personalized music. With Zenith, you'll never have to skip to
        find the perfect song again. Let's make your mood our muse!
      </p>
    </div>
  );
};

export default About;