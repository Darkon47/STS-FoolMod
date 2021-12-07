package eatyourbeets.cards.animator.series.Katanagatari;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.animator.special.Emonzaemon_EntouJyuu;
import eatyourbeets.cards.base.*;
import eatyourbeets.effects.AttackEffects;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.utilities.CardSelection;
import eatyourbeets.utilities.GameActions;

import java.util.ArrayList;
public class Emonzaemon extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Emonzaemon.class)
            .SetAttack(1, CardRarity.COMMON, EYBAttackType.Ranged)
            .SetSeriesFromClassPackage()
            .PostInitialize(data -> data.AddPreview(new Emonzaemon_EntouJyuu(), true));

    public Emonzaemon()
    {
        super(DATA);

        Initialize(4, 0);
        SetUpgrade(2, 0);

        SetAffinity_Green(2, 0, 1);
        SetAffinity_Dark(1);
        SetHitCount(2);
    }


    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.DealCardDamage(this, m, AttackEffects.GUNSHOT).forEach(d -> d.SetSoundPitch(0.55f, 0.65f));

        if (CheckSpecialCondition(true))
        {
            GameActions.Bottom.MakeCardInDrawPile(new Emonzaemon_EntouJyuu())
            .SetDestination(CardSelection.Random)
            .SetUpgrade(upgraded, false);
        }
    }

    @Override
    public void triggerOnManualDiscard()
    {
        super.triggerOnManualDiscard();

        GameActions.Bottom.AddAffinity(Affinity.Green, 1);
    }

    @Override
    public boolean CheckSpecialCondition(boolean tryUse)
    {
        if (CombatStats.CanActivateLimited(cardID))
        {
            final ArrayList<AbstractCard> cardsPlayed = AbstractDungeon.actionManager.cardsPlayedThisTurn;
            final int size = cardsPlayed.size();
            final int count = tryUse ? 3 : 2;
            if (size >= count)
            {
                boolean canActivate = true;
                for (int i = 1; i <= count; i++)
                {
                    if (cardsPlayed.get(size - i).type != CardType.ATTACK)
                    {
                        canActivate = false;
                    }
                }

                if (canActivate)
                {
                    return !tryUse || CombatStats.TryActivateLimited(cardID);
                }
            }
        }

        return false;
    }
}