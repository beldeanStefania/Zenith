import React, { useState, useEffect } from 'react';
import zenithText from '../../assets/Zenith-text.png';
import waveImg from '../../assets/Wave.png';
import constellationImg from '../../assets/Constellation.jpg';

interface ZenithIntroProps {
    onComplete: () => void;
}

const ZenithIntro: React.FC<ZenithIntroProps> = ({ onComplete }) => {
    const [isVisible, setIsVisible] = useState(false);
    const [startDistortion, setStartDistortion] = useState(false);

    useEffect(() => {
        setTimeout(() => {
            setIsVisible(true);
        }, 100);

        setTimeout(() => {
            setStartDistortion(true);
        }, 1000);

        setTimeout(() => {
            onComplete();
        }, 2500);
    }, [onComplete]);

    return (
        <div style={{
            position: 'fixed',
            top: 0,
            left: 0,
            width: '100vw',
            height: '100vh',
            backgroundColor: 'black',
            overflow: 'hidden',
            zIndex: 9999 // Ensure it's on top of everything
        }}>
            {/* Constellation Background with drift effect */}
            <div style={{
                position: 'absolute',
                top: '-10%',
                left: '-10%',
                width: '120%',
                height: '120%',
                opacity: isVisible ? 1 : 0,
                transition: 'opacity 1s ease',
                animation: isVisible ? 'constellationDrift 30s ease-in-out infinite' : 'none'
            }}>
                <img
                    src={constellationImg}
                    alt="Background"
                    style={{
                        width: '100%',
                        height: '100%',
                        objectFit: 'cover',
                        filter: 'brightness(0.8)'
                    }}
                />
            </div>

            {/* Wave with distortion */}
            <div style={{
                position: 'absolute',
                top: '50%',
                left: '50%',
                transform: `translate(-50%, ${isVisible ? '-50%' : '0%'})`,
                width: '60%',
                maxWidth: '800px',
                opacity: isVisible ? 1 : 0,
                transition: 'all 1s ease',
                transitionDelay: '0.5s',
                zIndex: 1,
                overflow: 'hidden'
            }}>
                <img
                    src={waveImg}
                    alt="Wave"
                    style={{
                        width: '100%',
                        filter: startDistortion ? 'url(#distortion)' : 'none',
                        animation: startDistortion ? 'scanline 3s linear infinite' : 'none'
                    }}
                />

                {/* SVG Filter for Wave Distortion */}
                <svg style={{ position: 'absolute', width: 0, height: 0 }}>
                    <defs>
                        <filter id="distortion">
                            <feTurbulence
                                type="turbulence"
                                baseFrequency="0.01 0.02"
                                numOctaves="2"
                                result="turbulence"
                            >
                                <animate
                                    attributeName="baseFrequency"
                                    dur="3s"
                                    values="0.01 0.02;0.02 0.04;0.01 0.02"
                                    repeatCount="indefinite"
                                />
                            </feTurbulence>
                            <feDisplacementMap
                                in2="turbulence"
                                in="SourceGraphic"
                                scale="15"
                                xChannelSelector="R"
                                yChannelSelector="G"
                            />
                        </filter>
                    </defs>
                </svg>

                {/* Wave Scanning Effect */}
                <div
                    style={{
                        position: 'absolute',
                        top: 0,
                        left: startDistortion ? '-100%' : '0',
                        width: '10%',
                        height: '100%',
                        background: 'linear-gradient(90deg, transparent, rgba(255,255,255,0.2), transparent)',
                        animation: startDistortion ? 'scan 3s linear infinite' : 'none',
                        pointerEvents: 'none'
                    }}
                />
            </div>

            {/* Zenith Text with glow effect */}
            <div style={{
                position: 'absolute',
                top: '50%',
                left: '50%',
                transform: `translate(-50%, -50%) scale(${isVisible ? 1 : 0.9})`,
                width: '300px',
                opacity: isVisible ? 1 : 0,
                transition: 'all 1s ease',
                transitionDelay: '1s',
                zIndex: 2,
                filter: startDistortion ? 'drop-shadow(0 0 10px rgba(255,255,255,0.5))' : 'none',
                animation: startDistortion ? 'textPulse 4s ease-in-out infinite' : 'none'
            }}>
                <img
                    src={zenithText}
                    alt="ZENITH"
                    style={{
                        width: '100%',
                        animation: startDistortion ? 'textDistort 5s ease-in-out infinite' : 'none'
                    }}
                />
            </div>

            {/* CSS Animations */}
            <style>
                {`
                    @keyframes scan {
                        from { left: -10%; }
                        to { left: 100%; }
                    }

                    @keyframes scanline {
                        0% { transform: translateX(0) skew(0deg); }
                        50% { transform: translateX(10px) skew(-5deg); }
                        100% { transform: translateX(0) skew(0deg); }
                    }

                    @keyframes constellationDrift {
                        0% { transform: translate(0, 0) rotate(0deg); }
                        25% { transform: translate(2%, 2%) rotate(0.5deg); }
                        50% { transform: translate(-1%, -1%) rotate(-0.5deg); }
                        75% { transform: translate(-2%, 1%) rotate(0.25deg); }
                        100% { transform: translate(0, 0) rotate(0deg); }
                    }

                    @keyframes textPulse {
                        0% { filter: drop-shadow(0 0 10px rgba(255,255,255,0.5)); }
                        50% { filter: drop-shadow(0 0 20px rgba(255,255,255,0.8)); }
                        100% { filter: drop-shadow(0 0 10px rgba(255,255,255,0.5)); }
                    }

                    @keyframes textDistort {
                        0% { transform: scale(1) translateY(0); }
                        25% { transform: scale(1.02) translateY(-2px); }
                        50% { transform: scale(0.98) translateY(2px); }
                        75% { transform: scale(1.02) translateY(-1px); }
                        100% { transform: scale(1) translateY(0); }
                    }

                    @keyframes waveFloat {
                        0% { transform: translateY(0) scale(1); }
                        25% { transform: translateY(-5px) scale(1.01); }
                        50% { transform: translateY(0) scale(0.99); }
                        75% { transform: translateY(5px) scale(1.01); }
                        100% { transform: translateY(0) scale(1); }
                    }

                    @keyframes gradientShift {
                        0% { transform: translateX(-100%); }
                        100% { transform: translateX(100%); }
                    }
                `}
            </style>
        </div>
    );
};

export default ZenithIntro;