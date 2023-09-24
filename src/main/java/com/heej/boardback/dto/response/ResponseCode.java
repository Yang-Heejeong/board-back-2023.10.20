package com.heej.boardback.dto.response;

public interface ResponseCode {

    String SUCCESS = "SU";
    
    String VALIDATION_FAILED = "VF";
    String DUPLICATED_EMAIL = "DE";
    String DUPLICATED_NICKNAME = "DN";
    String DUPLICATED_TEL_NUMBER = "DT";

    String DATABASE_ERROR = "DBE";

}
