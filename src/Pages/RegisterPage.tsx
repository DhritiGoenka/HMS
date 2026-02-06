import React, { useState } from 'react'
import { Button, PasswordInput, SegmentedControl, TextInput } from '@mantine/core'
import { IconHeartbeat } from '@tabler/icons-react'
import {useForm} from '@mantine/form';
import { Link, useNavigate } from 'react-router-dom';
import {registerUser} from '../Api/UserApi';
import { errorNotification, successNotification } from '../Utility/NotificationUtil';

const RegisterPage = () => {

    const navigate = useNavigate();
    const [loading,setLoading] = useState(false);

    const passwordRegex = /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[@$!%*?&])[A-Za-z\d@$!%*?&]{6,15}$/;

    const form = useForm({
        initialValues: {
            name:'',
            role:"PATIENT",
            email: '',
            password: '',
            confirmPassword:""
        },

        validate: {
            name: (value)=>(!value?"Name is required":null),
            email: (value) => (/^\S+@\S+$/.test(value) ? null : 'Invalid email'),
            password: (value) => {
                if (!value) return "Password is required";
                if (!passwordRegex.test(value))
                    return "Password must be 6-15 chars, include uppercase, lowercase, number & special characters";
                return null;
            },
            confirmPassword:(value,values)=>(value===values.password?null:"Passwords don't match")
        },
    });

    const handleSubmit = (values: typeof form.values) =>{
        setLoading(true);
        registerUser(values).then((data: any)=>{
            successNotification("Registered Successfully!!");
            navigate('/login');
        }).catch((error: any)=>{
            const message = error?.response?.data?.errorMessage ?? "Something went wrong";
            errorNotification(message);
        }).finally(()=>setLoading(false))
    }

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
                        Register
                    </div>
                    <SegmentedControl 
                        {...form.getInputProps("role")}
                        fullWidth size="md" radius="md" color='pink' bg="none"
                        className='[&_*]:!text-white border border-white'
                        data={[{label:'Patient',value:"PATIENT"},{label:'Doctor',value:"DOCTOR"},{label:'Admin',value:"ADMIN"}]} 
                    />
                    <TextInput
                        className='transition duration-300'
                        variant="unstyled"
                        size="md"
                        radius="md"
                        placeholder="Name"
                        {...form.getInputProps('name')}
                    />
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
                    <PasswordInput
                        className='transition duration-300'
                        variant="unstyled"
                        size="md"
                        radius="md"
                        placeholder="Confirm Password"
                        {...form.getInputProps('confirmPassword')}
                    />
                    <Button radius="md" size="md" type='submit' color='pink' loading={loading}>
                        Register
                    </Button>
                    <div className='text-neutral-100 text-sm self-center'>
                        Have an account?
                        <Link to="/login" className='hover:underline'>Login</Link>
                    </div>
                </form>
            </div>
        </div>
    )
}

export default RegisterPage
