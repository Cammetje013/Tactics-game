package tactics.ui;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

public class UnitActionMenu extends Table {

    public UnitActionMenu(Skin skin, Runnable onMove, Runnable onAttack, boolean canMove, boolean canAttack) {
        setBackground(skin.newDrawable("white", 0.12f, 0.12f, 0.12f, 0.95f));
        pad(4);

        TextButton moveButton = new TextButton("Move", skin);
        moveButton.setDisabled(!canMove);
        moveButton.setTouchable(canMove ? Touchable.enabled : Touchable.disabled);
        moveButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                onMove.run();
            }
        });
        add(moveButton).width(90).pad(2).row();

        TextButton attackButton = new TextButton("Attack", skin);
        attackButton.setDisabled(!canAttack);
        attackButton.setTouchable(canAttack ? Touchable.enabled : Touchable.disabled);
        attackButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                onAttack.run();
            }
        });
        add(attackButton).width(90).pad(2).row();

        pack();
    }
}
