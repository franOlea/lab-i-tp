package edu.palermo.lab.i.user.doctor.persistence.in.memory;

import edu.palermo.lab.i.user.doctor.DoctorDto;
import edu.palermo.lab.i.user.doctor.persistence.DoctorMetadataDao;
import lombok.NonNull;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class InMemoryDoctorMetadataDao implements DoctorMetadataDao {

  private final List<DoctorDto> doctors = new LinkedList<>();

  @Override
  public List<DoctorDto> getAll() {
    return doctors.stream()
        .map(DoctorDto::copy)
        .collect(Collectors.toList());
  }

  @Override
  public void save(@NonNull final DoctorDto metadata) {
    Optional<DoctorDto> potentialExistingDoctor = doctors.stream()
        .filter(savedDoctor -> savedDoctor.getUserDto().equals(metadata.getUserDto()))
        .findFirst();
    if(potentialExistingDoctor.isPresent()) {
      DoctorDto savedDoctor = potentialExistingDoctor.get();
      savedDoctor.setHourlyFee(metadata.getHourlyFee());
    } else {
      doctors.add(metadata);
    }
  }

  @Override
  public Optional<DoctorDto> getDoctorMetadata(@NonNull final String doctorId) {
    return doctors.stream()
        .filter(savedDoctor -> savedDoctor.getUserDto().getId().equals(doctorId))
        .findFirst()
        .map(DoctorDto::copy);
  }

}
