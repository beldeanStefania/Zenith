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
