import { Navigate, useLocation } from 'react-router-dom';

interface PrivateRouteProps {
    children: JSX.Element;
}

const PrivateRoute = ({ children }: PrivateRouteProps) => {
    const token = localStorage.getItem('token');
    const location = useLocation();

    if (!token) {
        return <Navigate to="/" state={{ from: location }} replace />;
    }

    return children;
}

export default PrivateRoute;