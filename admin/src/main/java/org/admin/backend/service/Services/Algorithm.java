package org.admin.backend.service.Services;

import lombok.RequiredArgsConstructor;
import org.admin.backend.service.dtos.Machine;
import org.admin.backend.service.dtos.MachineLimit;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@Service
@RequiredArgsConstructor
public class Algorithm {
    public static List<MachineLimit> findThroughputLimits(List<Machine> machines) {
        List<MachineLimit> limits = new ArrayList<>();
        for (Machine machine : machines) {
            if (machine.getCurrentThroughput() > machine.getRequiredThroughput()) {
                double ratio = machine.getCurrentThroughput() / machine.getRequiredThroughput();
                double limit = machine.getRequiredThroughput() + (machine.getCurrentThroughput() - machine.getRequiredThroughput()) / (1 + Math.exp(0.3 * (ratio - 1)));
                limits.add(new MachineLimit(machine.getHost(), limit));
            }
        }
        return limits;
    }
}
