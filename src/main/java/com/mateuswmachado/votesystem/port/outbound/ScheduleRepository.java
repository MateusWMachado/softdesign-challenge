package com.mateuswmachado.votesystem.port.outbound;

import com.mateuswmachado.votesystem.domain.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/** Interface representing a Schedule repository
 *
 * @author Mateus W. Machado
 * @since 17/07/2023
 * @version 1.0.0
 */

@Repository
public interface ScheduleRepository extends JpaRepository<Schedule, Long> {
}
