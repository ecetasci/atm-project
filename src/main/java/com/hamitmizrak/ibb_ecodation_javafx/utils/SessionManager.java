package com.hamitmizrak.ibb_ecodation_javafx.utils;

import com.hamitmizrak.ibb_ecodation_javafx.dto.UserDTO;

public class SessionManager {


        private static UserDTO currentUser;

        public static void setCurrentUser(UserDTO user) {
            currentUser = user;
        }

        public static UserDTO getCurrentUser() {
            return currentUser;
        }

        public static void clear() {
            currentUser = null;
        }
    }


