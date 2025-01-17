import { Swiper, SwiperSlide } from "swiper/react";

import "swiper/css";
import "swiper/css/effect-coverflow";
import "swiper/css/pagination";

import { EffectCoverflow, Pagination, Autoplay } from "swiper/modules";

import Survey from "./survey/Survey";
import content from "./content";
import "./SlideShower.css";
import SingIn from "./SingUp/SingUp";

const SliderShower = ({
  show,
  setShowSurvey,
  showSingUp,
  setShowSingUp,
}: {
  show: boolean;
  setShowSurvey: Function;
  showSingUp: boolean;
  setShowSingUp: Function;
}) => {
  return (
    <div className="container">
      <Swiper
        effect={"coverflow"}
        grabCursor={true}
        centeredSlides={true}
        loop={true}
        slidesPerView={"auto"}
        coverflowEffect={{
          scale: 0.8,
          depth: 150,
          slideShadows: false,
        }}
        autoplay={{
          delay: 2500,
          disableOnInteraction: false,
        }}
        modules={[EffectCoverflow, Autoplay]}
      >
        {content.map(({ img, text }, index) => (
          <SwiperSlide key={index}>
            <div className="slide">
              <div className="info">
                {(index === 0 || index === 5) && (
                  <>
                    <img src={img} className="img1" />
                    <div className="survey">
                      <Survey show={show} setShowSurvey={setShowSurvey} textButton={"Try now"}  styleButton="styleButton" />
                    </div>
                    <div className="text">
                      <p>{text}</p>
                    </div>
                  </>
                )}
                {(index === 1 || index === 6) && (
                  <>
                    <div className="text2">
                      <p>{text}</p>
                    </div>
                    <div className="singIn">
                      <button
                        onClick={() => setShowSingUp(true)}
                        className="styleButton"
                      >
                        Try now
                      </button>
                      {showSingUp && (
                        <SingIn show={showSingUp} setShow={setShowSingUp} />
                      )}
                    </div>
                    <img src={img} alt="profile" className="img" />
                  </>
                )}
                {index > 1 && (index < 5 || index > 6) && (
                  <>
                  <div className="text">
                    <p>{text}</p>
                  </div>
                  <img src={img} className="img1" />
                  </>
                )}
              </div>
            </div>
          </SwiperSlide>
        ))}
      </Swiper>
    </div>
  );
};

export default SliderShower;


/**
 * @fileoverview SlideShower component that creates an animated carousel of feature slides
 * Uses Swiper for carousel functionality with coverflow effect
 * @requires swiper
 * @requires swiper/css
 * @requires swiper/css/effect-coverflow
 * @requires swiper/css/pagination
 */

import { Swiper, SwiperSlide } from "swiper/react";

import "swiper/css";
import "swiper/css/effect-coverflow";
import "swiper/css/pagination";

import { EffectCoverflow, Pagination, Autoplay } from "swiper/modules";

import Survey from "./survey/Survey";
import content from "./content";
import "./SlideShower.css";
import SingIn from "./SingUp/SingUp";

/**
 * SlideShower component that displays a carousel of features with coverflow effect
 * Includes integrated survey and sign-up functionality on specific slides
 * Uses Swiper for smooth carousel animations and interactions
 * 
 * @component
 * @param {Object} props - Component properties
 * @param {boolean} props.show - Controls visibility of survey
 * @param {Function} props.setShowSurvey - Function to toggle survey visibility
 * @param {boolean} props.showSingUp - Controls visibility of sign-up modal
 * @param {Function} props.setShowSingUp - Function to toggle sign-up visibility
 * @returns {JSX.Element} A Swiper carousel with feature slides
 */

const SliderShower = ({
  show,
  setShowSurvey,
  showSingUp,
  setShowSingUp,
}: {
  show: boolean;
  setShowSurvey: Function;
  showSingUp: boolean;
  setShowSingUp: Function;
}) => {
  return (
    <div className="container">
      <Swiper
        effect={"coverflow"}
        grabCursor={true}
        centeredSlides={true}
        loop={true}
        slidesPerView={"auto"}
        coverflowEffect={{
          scale: 0.8,
          depth: 150,
          slideShadows: false,
        }}
        autoplay={{
          delay: 2500,
          disableOnInteraction: false,
        }}
        modules={[EffectCoverflow, Autoplay]}
      >
        {content.map(({ img, text }, index) => (
          <SwiperSlide key={index}>
            <div className="slide">
              <div className="info">
                {/* First and sixth slides with survey option */}
                {(index === 0 || index === 5) && (
                  <>
                    <img src={img} className="img1" />
                    <div className="survey">
                      <Survey show={show} setShowSurvey={setShowSurvey} textButton={"Try now"}  styleButton="styleButton" />
                    </div>
                    <div className="text">
                      <p>{text}</p>
                    </div>
                  </>
                )}
                {/* Second and seventh slides with sign up option */}
                {(index === 1 || index === 6) && (
                  <>
                    <div className="text2">
                      <p>{text}</p>
                    </div>
                    <div className="singIn">
                      <button
                        onClick={() => setShowSingUp(true)}
                        className="styleButton"
                      >
                        Try now
                      </button>
                      {showSingUp && (
                        <SingIn show={showSingUp} setShow={setShowSingUp} />
                      )}
                    </div>
                    <img src={img} alt="profile" className="img" />
                  </>
                )}
                {/* Regular feature slides */}
                {index > 1 && (index < 5 || index > 6) && (
                  <>
                  <div className="text">
                    <p>{text}</p>
                  </div>
                  <img src={img} className="img1" />
                  </>
                )}
              </div>
            </div>
          </SwiperSlide>
        ))}
      </Swiper>
    </div>
  );
};

export default SliderShower;