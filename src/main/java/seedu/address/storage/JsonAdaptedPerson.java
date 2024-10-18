package seedu.address.storage;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.model.appointment.Appointment;
import seedu.address.model.appointment.Date;
import seedu.address.model.appointment.From;
import seedu.address.model.appointment.To;
import seedu.address.model.person.*;
import seedu.address.model.tag.Tag;

/**
 * Jackson-friendly version of {@link Person}.
 */
class JsonAdaptedPerson {

    public static final String MISSING_FIELD_MESSAGE_FORMAT = "Person's %s field is missing!";

    private final String name;
    private final String phone;
    private final String email;
    private final String property;
    private final String date;
    private final String from;
    private final String to;

    private final String role;
    private final List<JsonAdaptedTag> tags = new ArrayList<>();

    /**
     * Constructs a {@code JsonAdaptedPerson} with the given person details.
     */
    @JsonCreator
    public JsonAdaptedPerson(@JsonProperty("name") String name,@JsonProperty("role") String role,
                             @JsonProperty("phone") String phone, @JsonProperty("date") String date,
                             @JsonProperty("email") String email, @JsonProperty("tags") List<JsonAdaptedTag> tags,
                             @JsonProperty("from") String from, @JsonProperty("to") String to,
                            @JsonProperty("property") String property) {
        this.name = name;
        this.role = role;
        this.phone = phone;
        this.email = email;
        this.date = date;
        this.from = from;
        this.to = to;
        this.property = property;
        if (tags != null) {
            this.tags.addAll(tags);
        }
    }

    /**
     * Converts a given {@code Person} into this class for Jackson use.
     */
    public JsonAdaptedPerson(Person source) {
        name = source.getName().fullName;
        if (source instanceof Buyer) {
            role = "buyer";
        } else {
            role = "seller";
        }
        phone = source.getPhone().value;
        email = source.getEmail().value;
        property = source.getProperty().toString();
        date = source.getAppointment().getDate().value;
        from = source.getAppointment().getFrom().value;
        to = source.getAppointment().getTo().value;
        tags.addAll(source.getTags().stream()
                  .map(JsonAdaptedTag::new)
                   .collect(Collectors.toList()));
    }

    /**
     * Converts this Jackson-friendly adapted person object into the model's {@code Person} object.
     *
     * @throws IllegalValueException if there were any data constraints violated in the adapted person.
     */
    public Person toModelType() throws IllegalValueException {
        final List<Tag> personTags = new ArrayList<>();
        for (JsonAdaptedTag tag : tags) {
            personTags.add(tag.toModelType());
        }

        if (name == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT, Name.class.getSimpleName()));
        }
        if (!Name.isValidName(name)) {
            throw new IllegalValueException(Name.MESSAGE_CONSTRAINTS);
        }
        final Name modelName = new Name(name);

        if (phone == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT, Phone.class.getSimpleName()));
        }
        if (!Phone.isValidPhone(phone)) {
            throw new IllegalValueException(Phone.MESSAGE_CONSTRAINTS);
        }
        final Phone modelPhone = new Phone(phone);

        if (email == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT, Email.class.getSimpleName()));
        }
        if (!Email.isValidEmail(email)) {
            throw new IllegalValueException(Email.MESSAGE_CONSTRAINTS);
        }
        final Email modelEmail = new Email(email);

        if (date == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT, Date.class.getSimpleName()));
        }

        if (from == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT, From.class.getSimpleName()));
        }

        if (to == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT, To.class.getSimpleName()));
        }

        final Appointment modelAppointment = new Appointment(new Date(date), new From(from), new To(to));

        if (property == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT,
                    Property.class.getSimpleName()));
        }
        final Property modelProperty = new Property(property);

        final Set<Tag> modelTags = new HashSet<>(personTags);
        if (role.equals("buyer")) {
            return new Buyer(modelName, modelPhone, modelEmail, modelTags, modelAppointment, modelProperty);
        } else {
            return new Seller(modelName, modelPhone, modelEmail, modelTags, modelAppointment, modelProperty);
        }
    }
}
