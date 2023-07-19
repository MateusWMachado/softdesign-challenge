package com.mateuswmachado.votesystem.port.inbound;

import com.mateuswmachado.votesystem.adapter.dto.VoteSessionDTO;
import com.mateuswmachado.votesystem.domain.VoteSession;
import com.mateuswmachado.votesystem.domain.exception.ScheduleNotFoundException;

public interface VoteSessionService {

    VoteSessionDTO openVotingSession(VoteSession voteSession) throws ScheduleNotFoundException;


}
