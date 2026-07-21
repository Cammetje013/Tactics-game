package tactics;

import javax.swing.*;

public class UnitActionMenu extends JPopupMenu {

    public UnitActionMenu(Runnable onMove, Runnable onAttack) {
        JMenuItem moveItem = new JMenuItem("Move");
        moveItem.addActionListener(e -> onMove.run());
        add(moveItem);

        JMenuItem attackItem = new JMenuItem("Attack");
        attackItem.addActionListener(e -> onAttack.run());
        add(attackItem);
    }
}