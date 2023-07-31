package py.com.daas.testfullstackjava.entities;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class UserStatusConverter implements AttributeConverter<UserStatus, String> {
    
    @Override
    public String convertToDatabaseColumn(UserStatus enumData) {
        if (enumData != null) return enumData.getName();
        else return null;
    }
 
    @Override
    public UserStatus convertToEntityAttribute(String dbData) {
        return UserStatus.find(dbData);
    }

}


