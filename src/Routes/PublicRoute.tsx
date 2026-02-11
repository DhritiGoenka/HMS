import { useSelector } from "react-redux";
import { Navigate, useNavigate } from "react-router-dom";
import {jwtDecode} from "jwt-decode";

interface PublicRouteProps{
    children:JSX.Element
}

const PublicRoute: React.FC<PublicRouteProps>=({children})=>{
    const token = useSelector((state:any)=>state.jwt);

    if(token){
        const user = jwtDecode(token) as any;
        return <Navigate to={`/${user?.role?.toLowerCase()}/dashboard`} />
    }
    return children;
}

export default PublicRoute;