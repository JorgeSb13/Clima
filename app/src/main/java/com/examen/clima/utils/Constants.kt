package com.examen.clima.utils

class Constants {

    companion object {
        const val API_KEY = "81532649e1644b68b0304012221602"
        const val N_FORECAST_DAYS = "8"

        const val ENTERED = 1
        const val NO_ENTERED = 3

        const val GOOGLE = 14
        const val FACEBOOK = 15

        const val LOCATION_REQ_CODE = 44

        // ----- Google -----
        const val RC_SIGN_IN = 100

        // ----- Preferences Helper -----
        const val SHARED_FILE_NAME = "share_preferences_file"
        const val KEY_HAS_ENTERED = "hasEntered"
        const val KEY_IS_LOGGED = "isLogged"

        // ----- TAGs -----
        const val TAG_LOCATION = "Location"

        // ----- Weather condition -----
        const val W_CLEAR = 1000L                       // Despejado
        const val W_P_CLOUDY = 1003L                    // Parcialmente nublado
        const val W_CLOUDY = 1006L                      // Nublado
        const val W_OVERCAST = 1009L                    // Nublado
        const val W_MIST = 1030L                        // Neblina
        const val W_RAIN_POSS = 1063L                   // Posibilidad de lluvia
        const val W_SNOW_POSS = 1066L                   // Posible caida nieve
        const val W_SLEET_POSS = 1069L                  // Posible caida de aguanieve
        const val W_FREEZING_DRIZZLE_POSS = 1072L       // Posibilidad de helada
        const val W_THUNDERY_OUT_POSS = 1087L           // Posible caida de rayos
        const val W_BLOWING_SNOW = 1114L                // Caida de nieve
        const val W_BLIZZARD = 1117L                    // Nevada
        const val W_FOG = 1135L                         // Niebla
        const val W_FREEZING_FOG = 1147L                // Niebla helada
        const val W_LIGHT_DRIZZLE_POSS = 1150L          // Posible llovizna
        const val W_LIGHT_DRIZZLE = 1153L               // Llovizna
        const val W_FREEZING_DRIZZLE = 1168L            // Llovizna helada
        const val W_H_FREEZING_DRIZZLE = 1171L          // Lluvia helada
        const val W_LIGHT_RAIN_POSS = 1180L             // Posibilidad de lluvia ligera
        const val W_LIGHT_RAIN = 1183L                  // Lluvia ligera
        const val W_MODERATE_RAIN_1 = 1186L             // Lluvia moderada
        const val W_MODERATE_RAIN_2 = 1189L             // Lluvia moderada
        const val W_HEAVY_RAIN_1 = 1192L                // Lluvia fuerte
        const val W_HEAVY_RAIN_2 = 1195L                // Lluvia fuerte
        const val W_L_FREEZING_RAIN = 1198L             // Lluvia helada ligera
        const val W_M_OR_L_FREEZING_RAIN = 1201L        // Lluvia helada moderada o intensa
        const val W_LIGHT_SLEET = 1204L                 // Caida ligera de aguanieve
        const val W_M_OR_H_SLEET = 1207L                // Caida moderada o intensa de aguanieve
        const val W_LIGHT_SNOW_POSS = 1210L             // Posible caida ligera de nieve
        const val W_LIGHT_SNOW = 1213L                  // Caida ligera de nieve
        const val W_MODERATE_SNOW_POSS = 1216L          // Posible caida moderada de nieve
        const val W_MODERATE_SNOW = 1219L               // Caida moderada de nieve
        const val W_HEAVY_SNOW_POSS = 1222L             // Posible caida intensa de nieve
        const val W_HEAVY_SNOW = 1225L                  // Caida intensa de nieve
        const val W_ICE_PELLETS = 1237L                 // Caida de gránulos de hielo
        const val W_LIGHT_RAIN_S = 1240L                // Lluvia ligera
        const val W_M_OR_H_RAIN = 1243L                 // Lluvia moderada o intensa
        const val W_TORRENTIAL_RAIN = 1246L             // Lluvia torrencial
        const val W_LIGHT_SLEET_S = 1249L               // Caida ligera de aguanieve
        const val W_M_OR_H_SLEET_S = 1252L              // Caida moderada o intensa de aguanieve
        const val W_LIGHT_SNOW_S = 1255L                // Caida ligera de nieve
        const val W_M_OR_H_SNOW_S = 1258L               // Caida moderada o intensa de nieve
        const val W_LIGHT_ICE_PELLETS = 1261L           // Caida ligera de gránulos de hielo
        const val W_M_OR_H_ICE_PELLETS = 1264L          // Caida moderada o intensa de gránulos de hielo
        const val W_LIGHT_RAIN_THUNDER_POSS = 1273L     // Posibilidad de lluvia ligera con truenos
        const val W_M_OR_H_RAIN_THUNDER = 1276L         // Lluvia moderada o intensa con truenos
        const val W_LIGHT_SNOW_THUNDER_POSS = 1279L     // Posible caida ligera de nieve con truenos
        const val W_M_OR_H_SNOW_THUNDER = 1282L         // Caida moderada o intensa de nieve con truenos
    }

}