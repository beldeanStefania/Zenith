/**
 * @fileoverview Global type declarations for the application
 * This file extends the global Window interface to include Spotify SDK types
 */

/**
 * Extends the global Window interface to include Spotify Web Playback SDK
 * @interface Window
 * @extends {Window}
 */
declare global {
    /**
     * Window interface extension
     * @interface Window
     * @property {typeof Spotify} Spotify - The Spotify Web Playback SDK object
     */
    interface Window {
        /**
         * The Spotify object containing the Web Playback SDK functionality
         * @type {typeof Spotify}
         */
        Spotify: typeof Spotify;
    }
}

/**
 * Export empty object to maintain module format
 * This is required for TypeScript module syntax
 */
export {};