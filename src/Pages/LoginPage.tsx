import { Button, PasswordInput, TextInput } from '@mantine/core'
import { IconHeartbeat } from '@tabler/icons-react'
import {useForm} from '@mantine/form';
import { Link, useNavigate } from 'react-router-dom';
import { loginUser } from '../Api/UserApi';
import { errorNotification, successNotification } from '../Utility/NotificationUtil';
import { useState } from 'react';
import { useDispatch } from 'react-redux';
import { setJwt } from '../Slices/JwtSlice';
import {jwtDecode} from 'jwt-decode';
import { setUser } from '../Slices/UserSlice';

const LoginPage = () => {

    const [loading,setLoading] = useState(false);
    const dispatch = useDispatch();
    const navigate = useNavigate();

    const form = useForm({
        initialValues: {
        email: '',
        password: '',
        },

        validate: {
        email: (value) => (/^\S+@\S+$/.test(value) ? null : 'Invalid email'),
        password:(value)=>(!value?"Password is required":null)
        },
    });

    const handleSubmit = (values: typeof form.values) =>{
        setLoading(true);
        loginUser(values).then((_data: any)=>{
            console.log(jwtDecode(_data))
            successNotification("Login Successful!!");
            dispatch(setJwt(_data));
            dispatch(setUser(jwtDecode(_data)));
        }).catch((error: any)=>{
            const message = error?.response?.data?.errorMessage ?? "Something went wrong";
            errorNotification(message);
        }).finally(()=>{
            setLoading(false);
        })
    };

    return (
        <div style={{background:'url("/bg.jpg")'}} 
            className='h-screen w-screen !bg-cover !bg-center !bg-no-repeat flex flex-col items-center justify-center'
        >
            <div className='text-pink-500 flex gap-1 items-center py-3'>
                <IconHeartbeat size={45} stroke={2.5}/>
                <span className='font-heading text-4xl font-semibold'> Pulse </span>
            </div>
            <div className='w-[450px] backdrop-blur-md p-10 py-8 rounded-lg'>
                <form 
                onSubmit = {form.onSubmit(handleSubmit)}
                className='flex flex-col gap-5
                [&_input]:placeholder-neutral-100
                [&_.mantine-Input-input]:!border-white
                focus-within:[&_.mantine-Input-input]:!border-pink-600
                [&_.mantine-Input-input]:!border
                [&_input]:!pl-2
                [&_svg]:text-white
                [&_input]:!text-white
                '>
                    <div className='self-center font-medium font-heading text-light text-xl'>
                        Login
                    </div>
                    <TextInput
                        className='transition duration-300'
                        variant="unstyled"
                        size="md"
                        radius="md"
                        placeholder="Email"
                        {...form.getInputProps('email')}
                    />
                    <PasswordInput
                        className='transition duration-300'
                        variant="unstyled"
                        size="md"
                        radius="md"
                        placeholder="Password"
                        {...form.getInputProps('password')}
                    />
                    <Button radius="md" size="md" type='submit' color='pink' loading={loading}>
                        Login
                    </Button>
                    <div className='text-neutral-100 text-sm self-center'>
                        New User?
                        <Link to="/register" className='hover:underline'> Register</Link>
                    </div>
                </form>
            </div>
        </div>
    )
}

export default LoginPage
