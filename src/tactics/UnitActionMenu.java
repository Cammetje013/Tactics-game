package tactics;

import javax.swing.*;

public class UnitActionMenu extends JPopupMenu {

    public UnitActionMenu(Runnable onMove, Runnable onAttack, boolean canMove, boolean canAttack) {
        JMenuItem moveItem = new JMenuItem("Move");
        moveItem.addActionListener(e -> onMove.run());
        moveItem.setEnabled(canMove);
        add(moveItem);

        JMenuItem attackItem = new JMenuItem("Attack");
        attackItem.addActionListener(e -> onAttack.run());
        attackItem.setEnabled(canAttack);
        add(attackItem);
    }
}
