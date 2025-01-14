/**
 * @fileoverview PrivateRoute component for handling protected routes and authentication
 * @requires react-router-dom
 */

import { Navigate, useLocation } from 'react-router-dom';

/**
 * Interface defining the props for the PrivateRoute component
 * @interface PrivateRouteProps
 * @property {JSX.Element} children - The child components to be rendered if authenticated
 */
interface PrivateRouteProps {
    children: JSX.Element;
}

/**
 * PrivateRoute component that protects routes requiring authentication
 * If user is not authenticated (no token present), redirects to home page
 * If authenticated, renders the protected child components
 * 
 * @component
 * @param {PrivateRouteProps} props - Component props containing children elements
 * @returns {JSX.Element} Either the protected route content or a redirect to home page
 * 
 * @example
 * ```tsx
 * <PrivateRoute>
 *   <ProtectedComponent />
 * </PrivateRoute>
 * ```
 */
const PrivateRoute = ({ children }: PrivateRouteProps) => {
    /**
     * Retrieves authentication token from localStorage
     * @constant
     * @type {string | null}
     */
    const token = localStorage.getItem('token');

    /**
     * Gets current location using useLocation hook
     * Used for redirect handling
     * @constant
     */
    const location = useLocation();

    // If no token is present, redirect to home page
    if (!token) {
        return <Navigate to="/" state={{ from: location }} replace />;
    }

    // If authenticated, render the protected route content
    return children;
}

export default PrivateRoute;