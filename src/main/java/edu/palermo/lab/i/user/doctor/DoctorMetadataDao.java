package edu.palermo.lab.i.user.doctor;

import lombok.NonNull;

import java.util.List;
import java.util.Optional;

public interface DoctorMetadataDao {

  List<DoctorMetadataDto> getAll();
  void save(@NonNull final DoctorMetadataDto metadata);
  Optional<DoctorMetadataDto> getDoctorMetadata(@NonNull final String doctorId);

}
