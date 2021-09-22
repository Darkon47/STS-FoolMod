package eatyourbeets.cards.base;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.interfaces.delegates.ActionT1;
import eatyourbeets.utilities.ColoredString;
import eatyourbeets.utilities.GameUtilities;

public class EYBCardCooldown
{


    private final static Color COOLDOWN_INCOMPLETE_COLOR = Settings.GREEN_TEXT_COLOR.cpy().lerp(Settings.CREAM_COLOR, 0.5f);
    private final ActionT1<AbstractMonster> onCooldownCompleted;
    private final EYBCard card;
    public final boolean canProgressOnManualDiscard;
    public final boolean canProgressFromExhaustPile;


    public EYBCardCooldown(EYBCard card, int baseCooldown, int cooldownUpgrade, ActionT1<AbstractMonster> onCooldownCompleted) {
        this(card,baseCooldown,cooldownUpgrade,onCooldownCompleted,false,false);
    }

    public EYBCardCooldown(EYBCard card, int baseCooldown, int cooldownUpgrade, ActionT1<AbstractMonster> onCooldownCompleted, boolean canProgressOnManualDiscard, boolean canProgressFromExhaustPile)
    {
        card.baseCooldownValue = card.cooldownValue = baseCooldown;
        card.upgrade_cooldownValue = cooldownUpgrade;
        this.onCooldownCompleted = onCooldownCompleted;
        this.canProgressOnManualDiscard = canProgressOnManualDiscard;
        this.canProgressFromExhaustPile = canProgressFromExhaustPile;
        this.card = card;
    }

    public ColoredString GetCooldownValueString()
    {
        if (card.cooldownValue < card.baseCooldownValue)
        {
            if (card.cooldownValue > 0)
            {
                return new ColoredString(card.cooldownValue, COOLDOWN_INCOMPLETE_COLOR);
            }
            else
            {
                return new ColoredString(card.cooldownValue, Settings.GREEN_TEXT_COLOR);
            }
        }
        else
        {
            return new ColoredString(card.cooldownValue, Settings.CREAM_COLOR);
        }
    }

    public void ProgressCooldownAndTrigger(AbstractMonster m)
    {
        ProgressCooldownAndTrigger(1, m);
    }

    public void ProgressCooldownAndTrigger(int progress, AbstractMonster m)
    {
        if (ProgressCooldown(progress))
        {
            if (m == null || GameUtilities.IsDeadOrEscaped(m))
            {
                onCooldownCompleted.Invoke(GameUtilities.GetRandomEnemy(true));
            }
            else
            {
                onCooldownCompleted.Invoke(m);
            }
        }
    }

    public boolean ProgressCooldown()
    {
        return ProgressCooldown(1);
    }

    public boolean ProgressCooldown(int amount)
    {
        boolean activate;
        int newValue;
        if (card.cooldownValue <= 0)
        {
            newValue = GetBase();
            activate = true;
        }
        else
        {
            newValue = Math.max(0, card.cooldownValue - amount);
            activate = false;
        }

        for (AbstractCard c : GameUtilities.GetAllInBattleInstances(card.uuid))
        {
            ((EYBCard) c).cooldownValue = newValue;
        }

        return activate;
    }

    public int GetCurrent()
    {
        return card.cooldownValue;
    }

    public int GetBase()
    {
        return card.baseCooldownValue;
    }
}