package eatyourbeets.powers.UnnamedReign;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.powers.StrengthPower;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.monsters.UnnamedReign.Shapes.Crystal.UltimateCrystal;
import eatyourbeets.powers.AnimatorPower;

public class UltimateCrystalPower extends AnimatorPower
{
    public static final String POWER_ID = CreateFullID(UltimateCrystalPower.class.getSimpleName());

    private static final int STRENGTH_GAIN = 2;

    public UltimateCrystalPower(AbstractCreature owner, int amount)
    {
        super(owner, POWER_ID);

        this.amount = amount;

        updateDescription();
    }

    @Override
    public void renderAmount(SpriteBatch sb, float x, float y, Color c)
    {
        if (amount >= 0)
        {
            FontHelper.renderFontRightTopAligned(sb, FontHelper.powerAmountFont, Integer.toString(this.amount), x, y, this.fontScale, c);
        }
        else
        {
            super.renderAmount(sb, x, y, c);
        }
    }

    @Override
    public void updateDescription()
    {
        String[] desc = powerStrings.DESCRIPTIONS;

        description = desc[0] + STRENGTH_GAIN + desc[1] + amount + desc[2];
    }

    @Override
    public void duringTurn()
    {
        super.duringTurn();

        if (this.amount > 0)
        {
            this.amount -= 1;
            updateDescription();
            if (this.amount <= 0)
            {
                ((UltimateCrystal)owner).SummonCopy();
            }
        }
    }

    @Override
    public void onAfterUseCard(AbstractCard card, UseCardAction action)
    {
        super.onAfterUseCard(card, action);

        GameActions.Bottom.StackPower(owner, new StrengthPower(owner, STRENGTH_GAIN));
    }

    @Override
    public void onDeath()
    {
        super.onDeath();

        AbstractPlayer p = AbstractDungeon.player;
        if (!AbstractDungeon.player.hasBlight(eatyourbeets.blights.animator.UltimateCrystal.ID))
        {
            AbstractDungeon.getCurrRoom().spawnBlightAndObtain(p.hb.cX, p.hb.cY, new eatyourbeets.blights.animator.UltimateCrystal());
        }
    }
}
