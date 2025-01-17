
/**
 * @fileoverview Utility functions for generating star field effects
 * Provides functionality to create CSS box-shadows for simulating stars
 */

/**
 * Generates a string of box-shadows to create a star field effect
 * Each shadow represents a single star with random positioning
 * 
 * @function generateBoxShadows
 * @param {number} count - The number of stars to generate
 * @returns {string} A CSS box-shadow string containing all star positions
 * 
 * @example
 * ```typescript
 * // Generate 700 stars
 * const starField = generateBoxShadows(700);
 * // Returns: "123px 456px #FFF, 789px 012px #FFF, ..."
 * ```
 * 
 * @remarks
 * - Stars are positioned within a 2000x2000 pixel area
 * - Each star is represented as a white (#FFF) box-shadow
 * - Positions are randomly generated using Math.random()
 * - The last shadow does not include a comma
 * - Used in con
 * */
export function generateBoxShadows(count: number): string {
  let shadows = '';
  for (let i = 0; i < count; i++) {
    // Generate random x and y coordinates within 2000px range
    const x = Math.floor(Math.random() * 2000);
    const y = Math.floor(Math.random() * 2000);
    // Add shadow to string, with comma if not last item
    shadows += `${x}px ${y}px #FFF${i < count - 1 ? ', ' : ''}`;
  }
  return shadows;
}