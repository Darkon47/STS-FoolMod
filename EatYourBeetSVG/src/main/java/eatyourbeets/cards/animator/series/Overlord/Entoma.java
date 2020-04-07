package eatyourbeets.cards.animator.series.Overlord;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.combat.BiteEffect;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.powers.PlayerStatistics;
import eatyourbeets.ui.EffectHistory;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameEffects;
import eatyourbeets.utilities.GameUtilities;

public class Entoma extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Entoma.class).SetAttack(1, CardRarity.COMMON);

    public Entoma()
    {
        super(DATA);

        Initialize(6, 0, 2);
        SetUpgrade(1, 0, 0);

        SetUnique(true, true);
        SetSynergy(Synergies.Overlord);
    }

    @Override
    protected void OnUpgrade()
    {
        if (timesUpgraded % 3 == 0)
        {
            upgradeMagicNumber(1);
        }

        upgradedMagicNumber = true;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        GameActions.Bottom.DealDamage(this, m, AbstractGameAction.AttackEffect.NONE)
        .SetDamageEffect(e -> GameEffects.List.Add(new BiteEffect(e.hb.cX, e.hb.cY - 40f * Settings.scale, Color.SCARLET.cpy())))
        .AddCallback(enemy ->
        {
            if (GameUtilities.TriggerOnKill(enemy, true) && EffectHistory.TryActivateLimited(cardID))
            {
                player.increaseMaxHp(2, false);

                GameActions.Bottom.ModifyAllInstances(uuid, AbstractCard::upgrade)
                .IncludeMasterDeck(true)
                .IsCancellable(false);
            }
        });

        GameActions.Bottom.ApplyPoison(p, m, magicNumber);
    }

    @Override
    public void atTurnStart()
    {
        super.atTurnStart();

        if (PlayerStatistics.TurnCount() > 0 && baseDamage > 0)
        {
            GameActions.Bottom.ModifyAllInstances(uuid, c -> c.baseDamage = Math.max(0, c.baseDamage - 1));
        }
    }
}