package eatyourbeets.cards.animator.beta.Colorless;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.rewards.RewardItem;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBAttackType;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.effects.vfx.SmallLaserEffect;
import eatyourbeets.rewards.animator.SakuraCardReward;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameEffects;
import eatyourbeets.utilities.GameUtilities;

import java.util.ArrayList;

public class SakuraKinomoto extends AnimatorCard //implements CustomSavable<SakuraCardReward>
{
    public static final EYBCardData DATA = Register(SakuraKinomoto.class).SetAttack(3, CardRarity.UNCOMMON, EYBAttackType.Elemental).SetColor(CardColor.COLORLESS);

    public SakuraKinomoto()
    {
        super(DATA);

        Initialize(17, 0, 0);
        SetUpgrade(3, 0, 0);
        SetScaling(1,0,0);

        //So she can't be created due to a bug with CustomSavable
        SetHealing(true);

        SetExhaust(true);
        SetSpellcaster();

        SetSynergy(Synergies.CardcaptorSakura);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        if (m != null)
        {
            GameActions.Bottom.DealDamage(this, m, AbstractGameAction.AttackEffect.NONE).SetDamageEffect(e -> GameEffects.Queue.Add(new SmallLaserEffect(player.hb.cX, player.hb.cY,
                    e.hb.cX + MathUtils.random(-0.05F, 0.05F), e.hb.cY + MathUtils.random(-0.05F, 0.05F), Color.PINK)))
                    .AddCallback(enemy ->
                    {
                        AbstractRoom room = GameUtilities.GetCurrentRoom();
                        if (true/*LZLZLZ(room instanceof MonsterRoomElite || room instanceof MonsterRoomBoss)
                                && GameUtilities.TriggerOnKill(enemy, false)
                                && CombatStats.TryActivateLimited(cardID)*/)
                        {
                            createSakuraReward();
                        }
                    });
            GameActions.Bottom.SFX("ORB_LIGHTNING_PASSIVE");
        }
    }

    private SakuraCardReward findSakuraReward()
    {
        ArrayList<RewardItem> rewards = AbstractDungeon.combatRewardScreen.rewards;

        for (RewardItem item : rewards)
        {
            if (item instanceof SakuraCardReward)
            {
                return (SakuraCardReward) item;
            }
        }

        return null;
    }

    private void createSakuraReward()
    {
        AbstractRoom room = GameUtilities.GetCurrentRoom();
        if (room.rewardAllowed)
        {
            RewardItem cardReward = new SakuraCardReward();
            int size = cardReward.cards.size();
            if (size > 0)
            {
                cardReward.cards.remove(size-1);
            }
            room.addCardReward(cardReward);
        }
    }

    /*@Override
    public SakuraCardReward onSave()
    {
        return findSakuraReward();
    }

    @Override
    public void onLoad(SakuraCardReward reward)
    {
        if (reward != null && !reward.cards.isEmpty())
        {
            AbstractDungeon.combatRewardScreen.rewards.add(reward);
        }
    }*/

}