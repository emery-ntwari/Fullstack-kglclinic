import React, { useState } from 'react'
import LibraryButton from './LibraryButton'

function LibraryLogin() {

   const [doYouHaveAnAccount, setDoYouHaveAnAccount] = useState(true);

   const [userName, setUserName] = useState("");
   const [password, setPassword] = useState("");
   const [studentId, setStudentId] = useState("");
   const [email, setEmail] = useState("");
   const [newPassword, setNewPassword] = useState("");
   const [confirmPassword, setConfirmPassword] = useState("");

   function navigateBetweenCreateAccount() {
        setDoYouHaveAnAccount(!doYouHaveAnAccount);
   }

   function loginAction() {
        alert("This is your username " + userName + " and password " + password);
   }

   function signUpAction() {
        if (!studentId.trim() || !email.trim() || !newPassword || !confirmPassword) {
            alert("Please complete all sign-up fields.");
            return;
        }

        if (newPassword !== confirmPassword) {
            alert("Passwords do not match.");
            return;
        }

        alert("Your details are valid. Account creation requires a backend service.");
   }


  return (
    <>
    { doYouHaveAnAccount ? (
                    <div style={{border:"3px solid blue", marginTop:"190px"}}>
                        <label>UserName:</label>
                        <input type="text" value={userName} onChange={(e) => setUserName(e.target.value)} placeholder='Enter Your username'/><br/>
                        <label>Password:</label>
                        <input type="password" value={password} onChange={(e) => setPassword(e.target.value)} /><br/>
                        <LibraryButton buttonLabel="LOGIN" onClickButton={loginAction}/> <br/>
                        <label>New here? </label> <LibraryButton buttonLabel="Create Account" onClickButton={navigateBetweenCreateAccount}/>
                    </div>
    ):(
            <div style={{border:"3px solid indigo", marginTop:"190px"}}>
                <label>Student ID:</label>
                <input type="text" value={studentId} onChange={(e) => setStudentId(e.target.value)} placeholder='Enter Your Student ID'/><br/>
                <label>Email:</label>
                <input type="email" value={email} onChange={(e) => setEmail(e.target.value)} /><br/>
                <label>Password:</label>
                <input type="password" value={newPassword} onChange={(e) => setNewPassword(e.target.value)} /><br/>
                <label>Confirm Password:</label>
                <input type="password" value={confirmPassword} onChange={(e) => setConfirmPassword(e.target.value)} /><br/>
                <LibraryButton buttonLabel="Sign up" onClickButton={signUpAction}/> <br/>
                 <label>Already have an account?? </label> <LibraryButton buttonLabel="Sign In" onClickButton={navigateBetweenCreateAccount}/>
            </div>
    )
     }
    </>
  )
}

export default LibraryLogin